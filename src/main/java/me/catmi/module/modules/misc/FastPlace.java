package me.catmi.module.modules.misc;

import me.catmi.settings.Setting;
import me.catmi.module.Module;
import net.minecraft.init.Items;

public class FastPlace extends Module{
	public FastPlace(){super("FastPlace", Category.Misc);}

	Setting.Boolean exp;
	Setting.Boolean crystals;
	Setting.Boolean everything;

	public void setup(){
		exp = registerBoolean("Exp", "Exp", false);
		crystals = registerBoolean("Crystals", "Crystals", false);
		everything = registerBoolean("Eveything", "Everything",false);
	}

	public void onUpdate(){
		if (exp.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE){
			mc.rightClickDelayTimer = 0;
		}

		if (crystals.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL){
			mc.rightClickDelayTimer = 0;
		}

		if (everything.getValue()){
			mc.rightClickDelayTimer = 0;
		}

		mc.playerController.blockHitDelay = 0;
	}
}