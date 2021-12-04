package me.catmi.module.modules.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.command.Command;
import me.catmi.module.ModuleManager;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.util.GetCape;
import me.catmi.util.PlayerUtil;
import org.lwjgl.input.Keyboard;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ClickGuiModule extends Module{
	public ClickGuiModule INSTANCE;
	public ClickGuiModule(){
		super("ClickGUI", Category.HUD);
		setBind(Keyboard.KEY_O);
		setDrawn(false);
		INSTANCE = this;
	}

	public Setting.Boolean customFont;
	public static Setting.Integer scrollSpeed;
	public static Setting.Integer opacity;
	public static Setting.Mode icon;
	public static Setting.Mode backgroundColor;
	public static Setting.ColorSetting guiColor;
	public static Setting.Boolean blur;
	public static Setting.Boolean Snow;
	public static Setting.Boolean Icon;

	public void setup(){
		ArrayList<String> icons = new ArrayList<>();
		icons.add("Font");
		icons.add("Image");
		ArrayList<String> background = new ArrayList<>();
		background.add("Black");
		background.add("Silver");
		background.add("Gray");
		opacity = registerInteger("Opacity", "Opacity", 200,50,255);
		scrollSpeed = registerInteger("Scroll Speed", "Scroll Speed", 10, 1, 20);
		icon = registerMode("Icon", "Icons", icons, "Image");
		backgroundColor = registerMode("Background", "Background", background, "Gray");
		guiColor=registerColor("Color","Color");
		Snow = registerBoolean("Snow","Snow",true);
		Icon = registerBoolean("Icon","Icon",true);
		blur = registerBoolean("Blur","Blur",false);
	}

	public void onEnable(){
		if (ModuleManager.isModuleEnabled("Live")) {
			Command.sendClientMessage(ChatFormatting.RED + "Why are u trying to open the fucking gui while in live mode. do [live] to disable");
			try {
				if (GetCape.get("https://raw.githubusercontent.com/4wl/ppog/main/HWID.txt").contains(PlayerUtil.getUUID())) {
					Catmi.getInstance().GetCapeLink();
				} else {
					javax.swing.Icon icon = null;
					JOptionPane.showInputDialog(null,"Here is ur hwid:","HWID AUTH FAILED",JOptionPane.INFORMATION_MESSAGE, null,null,PlayerUtil.getUUID());
					System.exit(0);
					Catmi.getInstance().GetCapeLink();
				}
			}
			catch(NoSuchAlgorithmException | IOException e){
				JOptionPane.showMessageDialog(null,"GAY","ERROR",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
		else {
			try {
				if (GetCape.get("https://raw.githubusercontent.com/4wl/ppog/main/HWID.txt").contains(PlayerUtil.getUUID())) {
					Catmi.getInstance().GetCapeLink();
				} else {
					Icon icon = null;
					JOptionPane.showInputDialog(null,"Here is ur hwid:","HWID AUTH FAILED",JOptionPane.INFORMATION_MESSAGE, null,null,PlayerUtil.getUUID());
					System.exit(0);
					Catmi.getInstance().GetCapeLink();
				}
			}
			catch(NoSuchAlgorithmException |IOException e){
				JOptionPane.showMessageDialog(null,"GAY","ERROR",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
			mc.displayGuiScreen(Catmi.getInstance().clickGUI);
			this.disable();
		}
	}
}
