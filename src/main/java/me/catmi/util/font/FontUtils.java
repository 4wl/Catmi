package me.catmi.util.font;

import me.catmi.util.CMColor;
import me.catmi.Catmi;
import net.minecraft.client.Minecraft;

public class FontUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static float drawStringWithShadow(boolean customFont, String text, int x, int y, CMColor color){
		if(customFont) return Catmi.fontRenderer.drawStringWithShadow(text, x, y, color);
		else return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
	}

	public static float drawStringWithShadow2(boolean customFont, String text, int x, int y, int color){
		if(customFont) return Catmi.fontRenderer.drawStringWithShadow2(text, x, y, color);
		else return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
	}

	public static int getStringWidth(boolean customFont, String str){
		if (customFont) return Catmi.fontRenderer.getStringWidth(str);
		else return mc.fontRenderer.getStringWidth(str);
	}

	public static int getFontHeight(boolean customFont){
		if (customFont) return Catmi.fontRenderer.getHeight();
		else return mc.fontRenderer.FONT_HEIGHT;
	}
}
