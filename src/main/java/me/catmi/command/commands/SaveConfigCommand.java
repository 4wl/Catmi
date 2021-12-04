package me.catmi.command.commands;

import me.catmi.Stopper;
import me.catmi.command.Command;

public class SaveConfigCommand extends Command{
	@Override
	public String[] getAlias(){
		return new String[]{"saveconfig", "savecfg"};
	}

	@Override
	public String getSyntax(){
		return "saveconfig";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		Stopper.saveConfig();
		Command.sendClientMessage("Config saved!");
	}
}
