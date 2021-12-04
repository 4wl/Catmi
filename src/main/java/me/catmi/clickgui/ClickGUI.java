package me.catmi.clickgui;

import me.catmi.Catmi;
import me.catmi.clickgui.frame.Component;
import me.catmi.clickgui.frame.Frames;
import me.catmi.clickgui.frame.Snow;
import me.catmi.module.Module;
import me.catmi.module.modules.hud.ClickGuiModule;
import me.catmi.util.GetCape;
import me.catmi.util.ParticleSystem;
import me.catmi.util.PlayerUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class ClickGUI extends GuiScreen {
	public static ArrayList<Frames> frames;
	private ArrayList<Snow> snowList = new ArrayList<Snow>();
	private static final ResourceLocation ckjm = new ResourceLocation("minecraft:logo.png");
	private ParticleSystem particleSystem;
	private boolean mouse;
	private boolean rainbow;
	private int dist;
	public int delta;

	public ClickGUI(){
		ClickGUI.frames = new ArrayList<Frames>();
		int DevFrameX = 10;
		for (final Module.Category category : Module.Category.values()){
			final Frames devframe = new Frames(category);
			devframe.setX(DevFrameX);
			ClickGUI.frames.add(devframe);
			DevFrameX += devframe.getWidth() + 10;
		}
		Random random = new Random();
		for (int i = 0; i < 100; ++i)
		{
			for (int y = 0; y < 3; ++y)
			{
				Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2)+1);
				snowList.add(snow);
			}
		}
	}

	public void drawCKJM(int x, int y){
		GlStateManager.enableAlpha();
		this.mc.getTextureManager().bindTexture(ckjm);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPushMatrix();
		Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,100,100,256,256);
		GL11.glPopMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		final ScaledResolution res = new ScaledResolution(mc);
		if (!snowList.isEmpty() && ClickGuiModule.Snow.getValue())
		{
			snowList.forEach(snow -> snow.Update(res));
		}
		if (ClickGuiModule.Icon.getValue()) {
			drawCKJM(100,100);
		}
		for (final Frames frames : ClickGUI.frames){
			frames.renderGUIFrame(this.fontRenderer);
			frames.updatePosition(mouseX, mouseY);
			frames.updateMouseWheel();
			for (final Component comp : frames.getComponents()){
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException{
		for (final Frames frames : ClickGUI.frames){
			if (frames.isWithinHeader(mouseX, mouseY) && mouseButton == 0){
				frames.setDrag(true);
				frames.dragX = mouseX - frames.getX();
				frames.dragY = mouseY - frames.getY();
			}
			if (frames.isWithinHeader(mouseX, mouseY) && mouseButton == 1){
				frames.setOpen(!frames.isOpen());
			}
			if (frames.isOpen() && !frames.getComponents().isEmpty()){
				for (final Component component : frames.getComponents()){
					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	protected void mouseReleased(final int mouseX, final int mouseY, final int state){
		for (final Frames frames : ClickGUI.frames){
			frames.setDrag(false);
		}
		for (final Frames frames : ClickGUI.frames){
			if (frames.isOpen() && !frames.getComponents().isEmpty()){
				for (final Component component : frames.getComponents()){
					component.mouseReleased(mouseX, mouseY, state);
				}
			}
		}
	}

	protected void keyTyped(final char typedChar, final int keyCode){
		for (final Frames frames : ClickGUI.frames){
			if (frames.isOpen() && !frames.getComponents().isEmpty()){
				for (final Component component : frames.getComponents()){
					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1){
			this.mc.displayGuiScreen(null);
		}
	}

	public boolean doesGuiPauseGame(){
		return false;
	}

	public void initGui(){
		particleSystem = new ParticleSystem(200, mouse, rainbow, dist);
		if (ClickGuiModule.blur.getValue()) {
			if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
				if (mc.entityRenderer.shaderGroup != null) {
					mc.entityRenderer.shaderGroup.deleteShaderGroup();
				}
				mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
		}
	}

	@Override
	public void onGuiClosed() {
		if (mc.entityRenderer.shaderGroup != null) {
			mc.entityRenderer.shaderGroup.deleteShaderGroup();
			mc.entityRenderer.shaderGroup = null;
		}
		super.onGuiClosed();

	}

	public static Frames getFrameByName(String name){
		Frames pa = null;

		for (Frames frames : getFrames()){
			if (name.equalsIgnoreCase(String.valueOf(frames.category))) pa = frames;
		}
		return pa;
	}

	public static ArrayList<Frames> getFrames(){
		return frames;
	}
}
