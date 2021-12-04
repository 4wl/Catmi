package me.catmi.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.module.modules.hud.ClickGuiModule;
import me.catmi.settings.Setting;
import me.catmi.util.Rainbow;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static me.catmi.module.modules.hud.HUD.customFont;

public class KeyBoard extends Module {
    public KeyBoard(){
        super("KeyBoard", Category.Render);
    }

    Setting.Integer x;
    Color c;
    Setting.Integer y;

    public void setup() {
        x = registerInteger("X","X",100,0,2000);
        y = registerInteger("Y","Y",100,0,2000);
    }

    private ResourceLocation getBox() {
        return new ResourceLocation("minecraft:key.png");
    }

    private void boxrender(int x,int y) {
        preboxrender1();
        ResourceLocation box1 = getBox();
        mc.renderEngine.bindTexture(box1);
        GuiIngame.drawScaledCustomSizeModalRect(x -38, y -10, 0.0F, 0.0F, 208, 208, 80, 80/*(size.getValue()).intValue(), (size.getValue()).intValue()*/, 208.0F, 208.0F);
        postboxrender1();
    }
    private static void preboxrender1() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
    }
    private static void postboxrender1() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    @Override
    public void onRender() {
        c = Rainbow.getColor();
        boxrender((x.getValue()), (y.getValue()));

        if (!mc.gameSettings.keyBindLeft.isKeyDown()) {
            drawStringWithShadow( "A", x.getValue() - 30, y.getValue() + 30, Color.white.getRGB());
        } else {
            drawStringWithShadow( "A", x.getValue() - 30, y.getValue() + 30, Color.green.getRGB());
        }
        if (!mc.gameSettings.keyBindBack.isKeyDown()) {
            drawStringWithShadow( "S", x.getValue(), y.getValue() + 30, Color.white.getRGB());
        } else {
            drawStringWithShadow( "S", x.getValue(), y.getValue() + 30, Color.green.getRGB());
        }
        if (!mc.gameSettings.keyBindRight.isKeyDown()) {
            drawStringWithShadow( "D", x.getValue() + 30, y.getValue() + 30, Color.white.getRGB());
        } else {
            drawStringWithShadow( "D", x.getValue() + 30, y.getValue() + 30, Color.green.getRGB());
        }
        if (!mc.gameSettings.keyBindForward.isKeyDown()) {
            drawStringWithShadow( "W", x.getValue() - 2, y.getValue(), Color.white.getRGB());
        } else {
            drawStringWithShadow( "W", x.getValue() - 2, y.getValue(), Color.green.getRGB());
        }
        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            drawStringWithShadow( "---------", x.getValue() - 10, y.getValue() + 55, Color.white.getRGB());
        } else {
            drawStringWithShadow( "---------", x.getValue() - 10, y.getValue() + 55, Color.green.getRGB());
        }
    }

    private void drawStringWithShadow (String text,int x, int y, int color) {
        if (customFont.getValue())
            Catmi.fontRenderer2.drawStringWithShadow(text, x, y, color);
        else
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }
    @Override
    public String getHudInfo(){

        return "[" + ChatFormatting.WHITE + "Green" + ChatFormatting.WHITE + "]";
    }

}
