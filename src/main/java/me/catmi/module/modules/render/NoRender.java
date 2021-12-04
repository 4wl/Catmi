package me.catmi.module.modules.render;

import me.catmi.event.events.BossbarEvent;
import me.catmi.event.events.PacketEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.material.Material;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class NoRender extends Module{
	public NoRender(){
		super("NoRender", Category.Render);
	}

	public Setting.Boolean armor;
	Setting.Boolean fire;
	Setting.Boolean blind;
	Setting.Boolean nausea;
	public Setting.Boolean hurtCam;
	public Setting.Boolean noOverlay;
	Setting.Boolean noBossBar;
	Setting.Boolean explosion;
	public Setting.Boolean noSkylight;

	public void setup(){
		armor = registerBoolean("Armor", "Armor", false);
		fire = registerBoolean("Fire", "Fire", false);
		blind = registerBoolean("Blind", "Blind", false);
		nausea = registerBoolean("Nausea", "Nausea", false);
		hurtCam = registerBoolean("HurtCam", "HurtCam", false);
		noSkylight = registerBoolean("Skylight", "Skylight", false);
		noOverlay = registerBoolean("No Overlay", "NoOverlay", false); //need to make sure this works better
		noBossBar = registerBoolean("No Boss Bar", "NoBossBar", false);
		explosion = registerBoolean("Explosion","Explosion",true);
	}

	public void onUpdate(){
		if (blind.getValue() && mc.player.isPotionActive(MobEffects.BLINDNESS)) mc.player.removePotionEffect(MobEffects.BLINDNESS);
		if (nausea.getValue() && mc.player.isPotionActive(MobEffects.NAUSEA)) mc.player.removePotionEffect(MobEffects.NAUSEA);
	}

	@EventHandler
	public Listener<RenderBlockOverlayEvent> blockOverlayEventListener = new Listener<>(event -> {
		if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) event.setCanceled(true);
		if (noOverlay.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER) event.setCanceled(true);
		if (noOverlay.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK) event.setCanceled(true);
	});

	// Disable Water and lava fog
	@EventHandler
	private final Listener<EntityViewRenderEvent.FogDensity> fogDensityListener = new Listener<>(event -> {
		if (noOverlay.getValue()){
			if (event.getState().getMaterial().equals(Material.WATER)
					|| event.getState().getMaterial().equals(Material.LAVA)){
				event.setDensity(0);
				event.setCanceled(true);
			}
		}
	});
	@EventHandler
	public Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
		Packet packet = event.getPacket();
		if (
				(packet instanceof SPacketExplosion && explosion.getValue()))
			event.cancel();
	});

	// Disable screen overlays Overlays
	@EventHandler
	private final Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener = new Listener<>(event -> {
		event.setCanceled(true);
	});

	@EventHandler
	private final Listener<RenderGameOverlayEvent> renderGameOverlayEventListener = new Listener<>(event -> {
		if (noOverlay.getValue()){
			if (event.getType().equals(RenderGameOverlayEvent.ElementType.HELMET)){
				event.setCanceled(true);
			}
			if (event.getType().equals(RenderGameOverlayEvent.ElementType.PORTAL)){
				event.setCanceled(true);
			}
		}
	});

	// Bossbar
	@EventHandler
	private final Listener<BossbarEvent> bossbarEventListener = new Listener<>(event -> {
		if (noBossBar.getValue()){
			event.cancel();
		}
	});

	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
	}
}
