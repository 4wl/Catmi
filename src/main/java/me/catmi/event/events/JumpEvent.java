package me.catmi.event.events;

import me.catmi.event.CatmiEvent;
import me.catmi.util.world.Location;

public class JumpEvent extends CatmiEvent {
	private Location location;

	public JumpEvent(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return this.location;
	}

	public void setLocation(Location location){
		this.location = location;
	}

}
