package me.catmi.event.events;

import me.catmi.event.CatmiEvent;

public class PlayerJoinEvent extends CatmiEvent {
	private final String name;

	public PlayerJoinEvent(String n){
		super();
		name = n;
	}

	public String getName(){
		return name;
	}
}
