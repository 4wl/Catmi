package me.catmi.util.config;

import me.catmi.players.enemy.Enemies;
import me.catmi.players.enemy.Enemy;
import me.catmi.players.friends.Friend;
import me.catmi.players.friends.Friends;
import me.catmi.Catmi;
import me.catmi.clickgui.frame.Frames;
import me.catmi.command.Command;
import me.catmi.clickgui.ClickGUI;
import me.catmi.macro.Macro;
import me.catmi.module.Module;
import me.catmi.module.ModuleManager;
import me.catmi.module.modules.misc.AutoGG;
import me.catmi.module.modules.misc.AutoReply;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;


public class SaveConfiguration{

	Minecraft mc = Minecraft.getMinecraft();

	//File Structure
	public static File CatmiDev;
		//Main file, %appdata%/.minecraft
	public static File Modules;
		//Inside main file, houses settings for modules
	public static File Messages;
		//Inside main file, houses settings for client messages such as AutoGG
	public static File Miscellaneous;
		//Inside main file, houses settings for client settings such as Font

	public static File Combat;
	public static File Exploits;
	public static File Hud;
	public static File Misc;
	public static File Movement;
	public static File Render;
		//Files inside the modules folder, houses module configs per category

	public SaveConfiguration(){
		CatmiDev = new File(mc.gameDir + File.separator + "Catmi");
		if (!CatmiDev.exists()){
			CatmiDev.mkdirs();
		}
		Modules = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules");
		if (!Modules.exists()){
			Modules.mkdirs();
		}
		Messages = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Messages");
		if (!Messages.exists()){
			Messages.mkdirs();
		}
		Miscellaneous = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Miscellaneous");
		if (!Miscellaneous.exists()){
			Miscellaneous.mkdirs();
		}
		Combat = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules" + File.separator + "Combat");
		if (!Combat.exists()){
			Combat.mkdirs();
		}
		Exploits = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules" + File.separator + "Exploits");
		if (!Exploits.exists()){
			Exploits.mkdirs();
		}
		Hud = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules" + File.separator + "Hud");
		if (!Hud.exists()){
			Hud.mkdirs();
		}
		Misc = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules" + File.separator + "Misc");
		if (!Misc.exists()){
			Misc.mkdirs();
		}
		Movement = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules" + File.separator + "Movement");
		if (!Movement.exists()){
			Movement.mkdirs();
		}
		Render = new File(mc.gameDir + File.separator + "Catmi" + File.separator + "Modules" + File.separator + "Render");
		if (!Render.exists()){
			Render.mkdirs();
		}
	}

	//saves gui settings
	public static void saveGUI(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(),"ClickGUI.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = ClickGUI.frames.iterator();
			while (var3.hasNext()){
				Frames frames = (Frames)var3.next();
				out.write(frames.category + ":" + frames.getX() + ":" + frames.getY() + ":" + frames.isOpen());
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves macros
	public static void saveMacros(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "ClientMacros.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = Catmi.getInstance().macroManager.getMacros().iterator();
			while(var3.hasNext()){
				Macro m = (Macro) var3.next();
				out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue().replace(" ", "_"));
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves friends
	public static void saveFriends(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "Friends.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = Friends.getFriends().iterator();
			while(var3.hasNext()){
				Friend f = (Friend)var3.next();
				out.write(f.getName());
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves enemies
	public static void saveEnemies(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "Enemies.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = Enemies.getEnemies().iterator();
			while(var3.hasNext()){
				Enemy e = (Enemy)var3.next();
				out.write(e.getName());
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves prefix
	public static void savePrefix(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "CommandPrefix.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(Command.getPrefix());
			out.write("\r\n");
			out.close();
		}
		catch (Exception var5){
		}
	}



	//saves AutoGG
	public static void saveAutoGG(){
		try{
			File file = new File(Messages.getAbsolutePath(), "AutoGG.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (String s : AutoGG.getAutoGgMessages()){
				out.write(s);
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves AutoReply
	public static void saveAutoReply(){
		try{
			File file = new File(Messages.getAbsolutePath(), "AutoReply.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(AutoReply.getReply());
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves client messages such as the watermark
	public static void saveMessages(){
		try{
			File file = new File(Messages.getAbsolutePath(), "ClientMessages.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(Command.MsgWaterMark + "");
			out.write(",");
			out.write(Command.cf.getName());
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves drawn modules
	public static void saveDrawn(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "DrawnModules.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = ModuleManager.getModules().iterator();
			while(var3.hasNext()){
				Module module = (Module)var3.next();
				out.write(module.getName() + ":" + module.isDrawn());
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves enabled/disabled modules
	public static void saveEnabled(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "EnabledModules.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = ModuleManager.getModules().iterator();
			while(var3.hasNext()){
				Module module = (Module)var3.next();
				if (module.isEnabled()){
					out.write(module.getName());
					out.write("\r\n");
				}
			}
			out.close();
		}
		catch (Exception var5){
		}
	}

	//saves module binds
	public static void saveBinds(){
		try{
			File file = new File(Miscellaneous.getAbsolutePath(), "ModuleBinds.json");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			Iterator var3 = ModuleManager.getModules().iterator();
			while(var3.hasNext()){
				Module module = (Module)var3.next();
				out.write(module.getName() + ":" + Keyboard.getKeyName(module.getBind()));
				out.write("\r\n");
			}
			out.close();
		}
		catch (Exception var5){
		}
	}
}