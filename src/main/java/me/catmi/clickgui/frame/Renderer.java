package me.catmi.clickgui.frame;

import me.catmi.util.CMColor;
import me.catmi.module.modules.hud.ClickGuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Renderer {

    //no gradient, single color
    public static void drawRectStatic(int leftX, int leftY, int rightX, int rightY, CMColor color){
        Gui.drawRect(leftX,leftY,rightX,rightY,color.getRGB());
    }

    //top down color gradient
    public static void drawRectGradient(int leftX, int leftY, int rightX, int rightY, CMColor startColor, CMColor endColor){
        float s = (float)(startColor.getRGB() >> 24 & 255) / 255.0F;
        float s1 = (float)(startColor.getRGB() >> 16 & 255) / 255.0F;
        float s2 = (float)(startColor.getRGB() >> 8 & 255) / 255.0F;
        float s3 = (float)(startColor.getRGB() & 255) / 255.0F;
        float e1 = (float)(endColor.getRGB() >> 24 & 255) / 255.0F;
        float e2 = (float)(endColor.getRGB() >> 16 & 255) / 255.0F;
        float e3 = (float)(endColor.getRGB() >> 8 & 255) / 255.0F;
        float e4 = (float)(endColor.getRGB() & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(rightX, leftY, 0).color(s1, s2, s3, s).endVertex(); //startColor
        bufferbuilder.pos(leftX, leftY, 0).color(s1, s2, s3, s).endVertex(); //startColor
        bufferbuilder.pos(leftX, rightY, 0).color(e2, e3, e4, e1).endVertex(); //endColor
        bufferbuilder.pos(rightX, rightY, 0).color(e2, e4, e4, e1).endVertex(); //endColor
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static CMColor getMainColor() {
        return ClickGuiModule.guiColor.getValue();
    }

    public static CMColor getTransColor (boolean hovered) {
        CMColor transColor = new CMColor(195, 195, 195, ClickGuiModule.opacity.getValue() - 50);

        if (ClickGuiModule.backgroundColor.getValue().equalsIgnoreCase("Black")){
            transColor = new CMColor(0, 0, 0,ClickGuiModule.opacity.getValue() - 50);
        }
        else if (ClickGuiModule.backgroundColor.getValue().equalsIgnoreCase("Silver")){
            transColor = new CMColor(100, 100, 100,ClickGuiModule.opacity.getValue() - 50);
        }
        else if (ClickGuiModule.backgroundColor.getValue().equalsIgnoreCase("PINK")) {
            transColor = new CMColor(255, 0, 255, ClickGuiModule.opacity.getValue() - 50);
        }

        if (hovered) return new CMColor(transColor.darker().darker());
        return transColor;
    }

    public static CMColor getFontColor() {
        return new CMColor(255,255,255);
    }
}