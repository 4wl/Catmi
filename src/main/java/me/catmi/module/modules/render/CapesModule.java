package me.catmi.module.modules.render;

import me.catmi.settings.Setting;
import me.catmi.module.Module;

import java.util.ArrayList;

public class CapesModule extends Module{
	public CapesModule(){
		super("Capes", Category.Render);
		setDrawn(false);
	}

	public Setting.Mode capeMode;

	public void setup(){
		ArrayList<String> CapeType = new ArrayList<>();
		CapeType.add("Catmi");
		CapeType.add("Emp");

		capeMode = registerMode("Type", "Type", CapeType, "Catmi");
	}
}
