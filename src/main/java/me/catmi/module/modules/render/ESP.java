package me.catmi.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.module.Module;
import me.catmi.settings.Setting;

import java.util.ArrayList;

public class ESP extends Module {

    public ESP() {
        super("ESP", Category.Render);
    }

    public static Setting.Integer Red;
    public static Setting.Integer Blue;
    public static Setting.Integer Green;
    public static Setting.Integer Red2;
    public static Setting.Integer Blue2;
    public static Setting.Integer Green2;

    public void setup() {
        width = this.registerDouble("Width", "Width",3 ,0.1, 10);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("OutLine");
        modes.add("WireFrame");
        mode = this.registerMode("RenderMode", "RenderMode",modes, "WireFrame");
        Red = registerInteger("Red","Red",255,0,255);
        Blue = registerInteger("Blue","Blue",0,0,255);
        Green = registerInteger("Green","Green",0,0,255);
        Red2 = registerInteger("FriendRed","Red2",255,0,255);
        Blue2 = registerInteger("FriendBlue","Blue2",0,0,255);
        Green2 = registerInteger("FriendGreen","Green2",0,0,255);
    }

    public static Setting.Mode mode;
    public static Setting.Double width;

    @Override
    public String getHudInfo() {
        String t = "";
        if (mode.getValue().equalsIgnoreCase("Outline")){
            t = "[" + ChatFormatting.WHITE + "Outline" + ChatFormatting.GRAY + "]";
        }
        if (mode.getValue().equalsIgnoreCase("WireFrame")){
            t = "[" + ChatFormatting.WHITE + "WireFrame" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}