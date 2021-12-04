package me.catmi.module.modules.hud;


import me.catmi.module.Module;
import me.catmi.settings.Setting;

public class RainbowOffset
        extends Module {
    public static Setting.Integer offset;

    public RainbowOffset() {
        super("RainbowOffset", Module.Category.HUD);
    }

    @Override
    public void setup() {
        offset = this.registerInteger("Offset", "Offset", 300, 1, 3000);
    }
}