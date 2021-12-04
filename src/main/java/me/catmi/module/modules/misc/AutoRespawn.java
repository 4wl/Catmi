package me.catmi.module.modules.misc;

import me.catmi.event.events.GuiScreenDisplayedEvent;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.command.Command;
import me.catmi.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module{
	public AutoRespawn(){
		super("AutoRespawn", Category.Misc);
	}

	Setting.Boolean coords;

	public void setup(){
		coords = registerBoolean("Coords", "Coords", false);
	}

	@EventHandler
	private final Listener<GuiScreenDisplayedEvent> listener = new Listener<>(event -> {
		if (event.getScreen() instanceof GuiGameOver){
			if (coords.getValue())
				Command.sendClientMessage(String.format("You died at x%d y%d z%d", (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ));
			if (mc.player != null)
				mc.player.respawnPlayer();
			mc.displayGuiScreen(null);
		}
	});

	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
	}
}