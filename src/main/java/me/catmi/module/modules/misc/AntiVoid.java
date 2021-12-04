package me.catmi.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.command.Command;
import me.catmi.module.Module;
import me.catmi.settings.Setting;
import me.catmi.util.Wrapper;

import java.util.ArrayList;

public class AntiVoid extends Module {
    Setting.Mode mode;

    public AntiVoid() {
        super("AntiVoid", Category.Misc);
    }

    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Bounce");
        modes.add("Mini");
        modes.add("Dolphin");
        this.mode = this.registerMode("Mode", "Mode",modes,"Bounce");
    }

    @Override
    public void onUpdate() {
        final Double yLevel = AntiVoid.mc.player.posY;
        if (yLevel <= 0.5) {
            Command.sendClientMessage("§aAttempting To Get " + ChatFormatting.RED + AntiVoid.mc.player.getName() + ChatFormatting.GREEN + " Out Of The void!");
            if (this.mode.getValue().equals("Bounce")) {
                AntiVoid.mc.player.moveVertical = 10.0f;
                AntiVoid.mc.player.jump();
            }
            if (this.mode.getValue().equals("Mini")) {
                AntiVoid.mc.player.moveVertical = 5.0f;
                AntiVoid.mc.player.jump();
            }
            if (this.mode.getValue().equals("Dolphin")) {
                AntiVoid.mc.player.moveVertical = 2.0f;
                AntiVoid.mc.player.jump();
            }
        }
    }

    @Override
    public String getHudInfo() {
        String t = "";
        if (mode.getValue().equalsIgnoreCase("Bounce")){
            t = "[" + ChatFormatting.WHITE + "Bounce" + ChatFormatting.GRAY + "]";
        }
        if (mode.getValue().equalsIgnoreCase("Mini")){
            t = "[" + ChatFormatting.WHITE + "Mini" + ChatFormatting.GRAY + "]";
        }
        if (mode.getValue().equalsIgnoreCase("Dolphin")){
            t = "[" + ChatFormatting.WHITE + "Dolphin" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
