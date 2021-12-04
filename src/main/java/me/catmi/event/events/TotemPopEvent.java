package me.catmi.event.events;

import me.catmi.event.CatmiEvent;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends CatmiEvent {

	private final Entity entity;

	public TotemPopEvent(Entity entity){
		super();
		this.entity = entity;
	}

	public Entity getEntity(){
		return entity;
	}
}
