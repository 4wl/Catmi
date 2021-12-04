package me.catmi.module.modules.render;

import me.catmi.event.events.PacketEvent;
import me.catmi.event.events.PlayerMoveEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

public class Freecam extends Module {
	public Freecam() {super("Freecam", Category.Render);}

	Setting.Boolean cancelPackets;
	Setting.Double speed;

	public void setup() {
		cancelPackets = registerBoolean("Cancel Packets","CancelPackets",true);
		speed = registerDouble("Speed", "Speed", 5.0D, 0.0D, 10.0D);
	}

	private double posX, posY, posZ;
	private float pitch, yaw;

	private EntityOtherPlayerMP clonedPlayer;

	private boolean isRidingEntity;
	private Entity ridingEntity;

	@Override
	protected void onEnable() {
		Catmi.EVENT_BUS.subscribe(this);
		if (mc.player != null) {
			isRidingEntity = mc.player.getRidingEntity() != null;

			if (mc.player.getRidingEntity() == null) {
				posX = mc.player.posX;
				posY = mc.player.posY;
				posZ = mc.player.posZ;
			} else {
				ridingEntity = mc.player.getRidingEntity();
				mc.player.dismountRidingEntity();
			}

			pitch = mc.player.rotationPitch;
			yaw = mc.player.rotationYaw;

			clonedPlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
			clonedPlayer.copyLocationAndAnglesFrom(mc.player);
			clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
			mc.world.addEntityToWorld(-100, clonedPlayer);
			mc.player.capabilities.isFlying = true;
			mc.player.capabilities.setFlySpeed((float) (speed.getValue() / 100f));
			mc.player.noClip = true;
		}
	}

	@Override
	protected void onDisable() {
		Catmi.EVENT_BUS.unsubscribe(this);
		EntityPlayer localPlayer = mc.player;
		if (localPlayer != null) {
			mc.player.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
			mc.world.removeEntityFromWorld(-100);
			clonedPlayer = null;
			posX = posY = posZ = 0.D;
			pitch = yaw = 0.f;
			mc.player.capabilities.isFlying = false;
			mc.player.capabilities.setFlySpeed(0.05f);
			mc.player.noClip = false;
			mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0.f;

			if (isRidingEntity) {
				mc.player.startRiding(ridingEntity, true);
			}
		}
	}

	@Override
	public void onUpdate() {
		mc.player.capabilities.isFlying = true;
		mc.player.capabilities.setFlySpeed((float) (speed.getValue() / 100f));
		mc.player.noClip = true;
		mc.player.onGround = false;
		mc.player.fallDistance = 0;
	}

	@EventHandler
	private final Listener<PlayerMoveEvent> moveListener = new Listener<>(event -> {
		mc.player.noClip = true;
	});

	@EventHandler
	private final Listener<PlayerSPPushOutOfBlocksEvent> pushListener = new Listener<>(event -> {
		event.setCanceled(true);
	});

	@EventHandler
	private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
		if ((event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) && cancelPackets.getValue()) {
			event.cancel();
		}
	});
}
