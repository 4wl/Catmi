package me.catmi.module.modules.movement;


import me.catmi.module.Module;
import me.catmi.settings.Setting;
import net.minecraft.init.Blocks;


public  class IceSpeed extends Module {


    private Setting.Double slipperiness;

    public IceSpeed(){
        super("IceSpeed", Category.Movement);



        Setting.Boolean slipperiness;

    }

    public void setup() {

        slipperiness = registerDouble("Slipperiness", "Slipperiness", 0.2f, 0.4f, 0.1f);
    }
        public void onUpdate() {
            Blocks.ICE.slipperiness = (float) slipperiness.getValue();
            Blocks.PACKED_ICE.slipperiness = (float) slipperiness.getValue();
            Blocks.FROSTED_ICE.slipperiness = (float) slipperiness.getValue();
        }

        public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;

    }

}