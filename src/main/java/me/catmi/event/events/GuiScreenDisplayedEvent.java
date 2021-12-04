package me.catmi.event.events;

import me.catmi.event.CatmiEvent;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenDisplayedEvent extends CatmiEvent {
	private final GuiScreen guiScreen;
	public GuiScreenDisplayedEvent(GuiScreen screen){
		super();
		guiScreen = screen;
	}

	public GuiScreen getScreen(){
		return guiScreen;
	}
}