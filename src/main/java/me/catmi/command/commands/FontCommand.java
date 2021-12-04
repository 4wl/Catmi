package me.catmi.command.commands;

import me.catmi.Catmi;
import me.catmi.command.Command;
import me.catmi.util.font.CFontRenderer;

import java.awt.*;

public class FontCommand extends Command{
	@Override
	public String[] getAlias(){
		return new String[]{
				"font", "setfont"
		};
	}

	@Override
	public String getSyntax(){
		return "font <Name> <Size>";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception{
		String font = args[0].replace("_", " ");
		int size = Integer.parseInt(args[1]);
		Catmi.fontRenderer = new CFontRenderer(new Font(font, Font.PLAIN, size), true, false);
		Catmi.fontRenderer.setFontName(font);
		Catmi.fontRenderer.setFontSize(size);
	}
}
