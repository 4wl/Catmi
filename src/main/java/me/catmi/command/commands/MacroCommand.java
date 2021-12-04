package me.catmi.command.commands;

import me.catmi.Catmi;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.command.Command;
import me.catmi.macro.Macro;
import org.lwjgl.input.Keyboard;

public class MacroCommand extends Command{
	@Override
	public String[] getAlias(){
		return new String[]{"macro", "macros"};
	}

	@Override
	public String getSyntax(){
		return "macro <add | del> <key> <value>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		if (args[0].equalsIgnoreCase("add")){
			Catmi.getInstance().macroManager.delMacro(Catmi.getInstance().macroManager.getMacroByKey(Keyboard.getKeyIndex(args[1])));
			Catmi.getInstance().macroManager.addMacro(new Macro(Keyboard.getKeyIndex(args[1].toUpperCase()), args[2].replace("_", " ")));
			Command.sendClientMessage(ChatFormatting.GREEN + "Added" + ChatFormatting.GRAY + " macro for key \"" + args[1].toUpperCase() + "\" with value \"" + args[2].replace("_", " ") + "\".");
		}
		if (args[0].equalsIgnoreCase("del")){
			if (Catmi.getInstance().macroManager.getMacros().contains(Catmi.getInstance().macroManager.getMacroByKey(Keyboard.getKeyIndex(args[1].toUpperCase())))){
				Catmi.getInstance().macroManager.delMacro(Catmi.getInstance().macroManager.getMacroByKey(Keyboard.getKeyIndex(args[1].toUpperCase())));
				Command.sendClientMessage(ChatFormatting.RED + "Removed " + ChatFormatting.GRAY + "macro for key \"" + args[1].toUpperCase() + "\".");
			}else{
				Command.sendClientMessage(ChatFormatting.GRAY + "That macro doesn't exist!");
			}
		}
	}
}
