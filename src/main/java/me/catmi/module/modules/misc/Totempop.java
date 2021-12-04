package me.catmi.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.event.CatmiEvent;
import me.catmi.module.Module;
import me.catmi.util.WurstplusFriendUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.codec.digest.Md5Crypt;

import java.util.HashMap;

public class Totempop extends Module {

public Totempop(){
    super("Totempop",Category.Misc); }

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<String, Integer>();
    public static ChatFormatting red = ChatFormatting.RED;
    public static ChatFormatting green = ChatFormatting.GREEN;
    public static ChatFormatting gold = ChatFormatting.GOLD;
    public static ChatFormatting grey = ChatFormatting.GRAY;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting reset = ChatFormatting.RESET;



public void setTotem_pop_counter(){

}









    public void update() {

        for (EntityPlayer player : mc.world.playerEntities) {

            if (!totem_pop_counter.containsKey(player.getName())) continue;

            if (player.isDead || player.getHealth() <= 0) {

                int count = totem_pop_counter.get(player.getName());

                totem_pop_counter.remove(player.getName());

                if (player == mc.player) continue;

                if (WurstplusFriendUtil.isFriend(player.getName())) {
                    WurstplusMessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "dude, " + bold + green + player.getName() + reset + " just fucking DIED after popping " + bold + count + reset + " totems. RIP :pray:");
                } else {
                    WurstplusMessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "dude, " + bold + red + player.getName() + reset + " just fucking DIED after popping " + bold + count + reset + " totems");
                }

            }

        }

    }

    private static class WurstplusMessageUtil {
        public static void send_client_message(String s) {
        }
    }
}

