package me.catmi.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;

public class AntiWeb extends Module {
    public AntiWeb() {
        super("AntiWeb", Category.Misc);
    }

    @Override
    public void onUpdate() {
        if (AntiWeb.mc.player.isInWeb) {
            final EntityPlayerSP player = AntiWeb.mc.player;
            --player.motionY;
            final EntityPlayerSP player2 = AntiWeb.mc.player;
            --player2.motionY;
            final EntityPlayerSP player3 = AntiWeb.mc.player;
            --player3.motionY;
            final EntityPlayerSP player4 = AntiWeb.mc.player;
            --player4.motionY;
        }
    }

    @Override
    public String getHudInfo(){
    return    "[" + ChatFormatting.WHITE + "NCP" + ChatFormatting.GRAY + "]";
    }

}