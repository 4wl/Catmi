package me.catmi.event.events;

import me.catmi.event.CatmiEvent;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends CatmiEvent {
	private final EnumHandSide handSide;

	public TransformSideFirstPersonEvent(EnumHandSide handSide){
		this.handSide = handSide;
	}

	public EnumHandSide getHandSide(){
		return handSide;
	}
}
