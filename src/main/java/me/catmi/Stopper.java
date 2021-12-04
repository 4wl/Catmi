package me.catmi;

import me.catmi.util.config.SaveConfiguration;

public class Stopper extends Thread{

	@Override
	public void run(){
		saveConfig();
	}

	public static void saveConfig(){

		Catmi.getInstance().saveModules.saveModules();

		SaveConfiguration.saveAutoGG();
		SaveConfiguration.saveAutoReply();
		SaveConfiguration.saveBinds();
		SaveConfiguration.saveDrawn();
		SaveConfiguration.saveEnabled();
		SaveConfiguration.saveEnemies();
		SaveConfiguration.saveFriends();
		SaveConfiguration.saveGUI();
		SaveConfiguration.saveMacros();
		SaveConfiguration.saveMessages();
		SaveConfiguration.savePrefix();
	}
}
