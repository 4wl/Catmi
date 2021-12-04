package me.catmi.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.Catmi;
import me.catmi.event.events.PacketEvent;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.world.World;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class FakeLag extends Module
{
    EntityOtherPlayerMP entity;
    private final Queue<Packet> packets;
    @EventHandler
    private final Listener<PacketEvent.Send> packetSendListener;

    public FakeLag() {
        super("FakeLag", Category.Misc);
        this.packets = new ConcurrentLinkedQueue<Packet>();
        final Packet[] packet = new Packet[1];
        this.packetSendListener = new Listener<PacketEvent.Send>(event -> {
            packet[0] = event.getPacket();
            if (!(packet[0] instanceof CPacketChatMessage) && !(packet[0] instanceof CPacketConfirmTeleport) && !(packet[0] instanceof CPacketKeepAlive) && !(packet[0] instanceof CPacketTabComplete) && !(packet[0] instanceof CPacketClientStatus)) {
                this.packets.add(packet[0]);
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }

    public void onEnable() {
        Catmi.EVENT_BUS.subscribe(this);
        (this.entity = new EntityOtherPlayerMP((World)FakeLag.mc.world, FakeLag.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)FakeLag.mc.player);
        this.entity.rotationYaw = FakeLag.mc.player.rotationYaw;
        this.entity.rotationYawHead = FakeLag.mc.player.rotationYawHead;
        FakeLag.mc.world.addEntityToWorld(666, (Entity)this.entity);
    }

    public void onDisable() {
        Catmi.EVENT_BUS.unsubscribe(this);
        if (this.entity != null) {
            FakeLag.mc.world.removeEntity((Entity)this.entity);
        }
        if (this.packets.size() > 0) {
            for (final Packet packet : this.packets) {
                FakeLag.mc.player.connection.sendPacket(packet);
            }
            this.packets.clear();
        }
    }

    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + this.packets.size() + ChatFormatting.GRAY + "]";
    }
}