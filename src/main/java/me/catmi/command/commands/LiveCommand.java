package me.catmi.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.command.Command;
import me.catmi.module.ModuleManager;
import me.catmi.module.modules.misc.Live;
import org.lwjgl.input.Keyboard;

public class LiveCommand extends Command {
    boolean found;
    @Override
    public String[] getAlias() {
        return new String[]{"live", "stream"};
    }

    @Override
    public String getSyntax() {
        return "-live";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        found = false;
        ModuleManager.getModules().forEach(m -> {
            if(m.getName().equalsIgnoreCase("Live")){
                if(m.isEnabled()){
                    m.disable();
                    found = true;
                    Command.sendClientMessage(ChatFormatting.DARK_RED + "Live Mode Off");
                } else if(!m.isEnabled()){
                    m.enable();
                    found = true;
                    Command.sendClientMessage(ChatFormatting.GREEN + "Live Mode On");
                }
            }
        });
    }


}