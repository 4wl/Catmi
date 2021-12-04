package me.catmi.module.modules.misc;

import me.catmi.event.events.PacketEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;

// @see me.catmi.mixin.mixins.MixinNetworkManager for PacketKick

public class NoKick extends Module{
	public NoKick(){super("NoKick", Category.Misc);}

	public Setting.Boolean noPacketKick;
	Setting.Boolean noSlimeCrash;
	Setting.Boolean noOffhandCrash;

	public void setup(){
		noPacketKick = registerBoolean("Packet", "Packet", true);
		noSlimeCrash = registerBoolean("Slime", "Slime", false);
		noOffhandCrash = registerBoolean("Offhand", "Offhand", false);
	}

	//slime
	public void onUpdate(){
		if (mc.world != null && noSlimeCrash.getValue()){
			mc.world.loadedEntityList
					.forEach(entity -> {
						if (entity instanceof EntitySlime){
							EntitySlime slime = (EntitySlime) entity;
							if (slime.getSlimeSize() > 4){
								mc.world.removeEntity(entity);
							}
						}
					});
		}
	}

	//Offhand
	@EventHandler
	private final Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
		if (noOffhandCrash.getValue()){
			if (event.getPacket() instanceof SPacketSoundEffect){
				if (((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC){
					event.cancel();
				}
			}
		}
	});

	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
	}
}