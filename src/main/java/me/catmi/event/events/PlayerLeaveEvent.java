package me.catmi.event.events;

import me.catmi.event.CatmiEvent;

public class PlayerLeaveEvent extends CatmiEvent {

	private final String name;

	public PlayerLeaveEvent(String n){
		super();
		name = n;
	}

	public String getName(){
		return name;
	}
}
