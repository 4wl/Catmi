package me.catmi.module.modules.misc;

import me.catmi.settings.Setting;
import me.catmi.module.Module;
import net.minecraft.item.ItemPickaxe;

public class NoEntityTrace extends Module{
	public NoEntityTrace(){
		super("NoEntityTrace", Category.Misc);
	}

	Setting.Boolean pickaxeOnly;

	public void setup(){
		pickaxeOnly = registerBoolean("Pickaxe Only", "PickaxeOnly", true);
	}

	boolean isHoldingPickaxe = false;

	public void onUpdate(){
		isHoldingPickaxe = mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe;
	}

	public boolean noTrace(){
		if (pickaxeOnly.getValue()) return isEnabled() && isHoldingPickaxe;
		return isEnabled();
	}
}

