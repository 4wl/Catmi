package me.catmi.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

/**
* @author lukflug
*/
// Why would anyone ever need to use JavaDoc properly?

public class CMColor extends Color {
	public CMColor(int rgb) {
		super(rgb);
	}
	
	public CMColor(int rgba, boolean hasalpha) {
		super(rgba,hasalpha);
	}
	
	public CMColor(int r, int g, int b) {
		super(r,g,b);
	}
	
	public CMColor(int r, int g, int b, int a) {
		super(r,g,b,a);
	}
	
	public CMColor(Color color) {
		super(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
	}
	
	public CMColor(CMColor color, int a) {
		super(color.getRed(),color.getGreen(),color.getBlue(),a);
	}
	
	public static CMColor fromHSB (float hue, float saturation, float brightness) {
		return new CMColor(Color.getHSBColor(hue,saturation,brightness));
	}
	
	public float getHue() {
		return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[0];
	}
	
	public float getSaturation() {
		return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[1];
	}
	
	public float getBrightness() {
		return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[2];
	}
	
	public void glColor() {
		GL11.glColor4f(getRed()/255.0f,getGreen()/255.0f,getBlue()/255.0f,getAlpha()/255.0f);
	}
}
