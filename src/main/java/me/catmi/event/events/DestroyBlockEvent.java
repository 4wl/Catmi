package me.catmi.event.events;

import me.catmi.event.CatmiEvent;
import net.minecraft.util.math.BlockPos;

public class DestroyBlockEvent extends CatmiEvent {

	BlockPos pos;

	public DestroyBlockEvent(BlockPos blockPos){
		super();
		pos = blockPos;
	}

	public BlockPos getBlockPos(){
		return pos;
	}

	public void setPos(BlockPos pos){
		this.pos = pos;
	}
}
