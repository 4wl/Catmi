package me.catmi.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBUniformBufferObject.glGetInteger;
import static org.lwjgl.opengl.ARBViewportArray.glGetFloat;
import static org.lwjgl.opengl.GL11.*;

public final class RenderUtil
{

    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);


    public static void DrawPolygon(double x, double y, int radius, int sides, int color)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        bufferbuilder.pos(x, y, 0).endVertex();
        final double TWICE_PI = Math.PI * 2;

        for (int i = 0; i <= sides; i++)
        {
            double angle = (TWICE_PI * i / sides) + Math.toRadians(180);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0).endVertex();
        }
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawRect(float x, float y, float w, float h, int color)
    {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) x, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x, float y, float w, float h, int color, float alpha)
    {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) x, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) w, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void drawRect(int x, int y, int x1, int y1, int color, int p_CustomAlpha)
    {
        setupOverlayRendering();
        disableDefaults();
        GL11.glColor4d(getRedFromHex(color), getGreenFromHex(color), getBlueFromHex(color), p_CustomAlpha > 0 ? p_CustomAlpha : getAlphaFromHex(color));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x1, y);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y1);
        GL11.glVertex2i(x1, y1);
        GL11.glEnd();
        enableDefaults();
    }

    public static void setupOverlayRendering()
    {
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, getScreenWidth(), getScreenHeight(), 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
    }

    public static void disableDefaults()
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void enableDefaults()
    {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public static int getScreenWidth()
    {
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        return (Math.round(viewport.get(2)));
    }

    public static int getScreenHeight()
    {
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        return (Math.round(viewport.get(3)));
    }

    public static double getAlphaFromHex(int color)
    {
        return ((double) ((color >> 24 & 0xff) / 255F));
    }

    public static double getRedFromHex(int color)
    {
        return ((double) ((color >> 16 & 0xff) / 255F));
    }

    public static double getGreenFromHex(int color)
    {
        return ((double) ((color >> 8 & 0xff) / 255F));
    }

    public static double getBlueFromHex(int color)
    {
        return ((double) ((color & 0xff) / 255F));
    }


    public static void drawRoundedRect(int x, int y, int x1, int y1, int radius, int color, int p_CustomAlpha)
    {
        disableDefaults();
        float newX = Math.abs(x + radius);
        float newY = Math.abs(y + radius);
        float newX1 = Math.abs(x1 - radius);
        float newY1 = Math.abs(y1 - radius);

        drawRect(newX, newY, newX1, newY1, color);
        drawRect(x, newY, newX, newY1, color);
        drawRect(newX1, newY, x1, newY1, color);
        drawRect(newX, y, newX1, newY, color);
        drawRect(newX, newY1, newX1, y1, color);

        // Draw curves
        drawQuarterCircle((int) newX, (int) newY, radius, 0, color, p_CustomAlpha);
        drawQuarterCircle((int) newX1, (int) newY, radius, 1, color, p_CustomAlpha);
        drawQuarterCircle((int) newX, (int) newY1, radius, 2, color, p_CustomAlpha);
        drawQuarterCircle((int) newX1, (int) newY1, radius, 3, color, p_CustomAlpha);
        enableDefaults();
    }

    public static void drawQuarterCircle(int x, int y, int radius, int mode, int color, int p_CustomAlpha)
    {
        disableDefaults();
        GL11.glColor4d(getRedFromHex(color), getGreenFromHex(color), getBlueFromHex(color), p_CustomAlpha > 0 ? p_CustomAlpha : getAlphaFromHex(color));
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2d(x, y);
        if (mode == 0)
        {
            for (int i = 0; i <= 90; i++)
            {
                GL11.glVertex2d(x + (Math.sin((i * 3.141526D / 180)) * (radius * -1)), y + (Math.cos((i * 3.141526D / 180)) * (radius * -1)));
            }
        }
        else if (mode == 1)
        {
            for (int i = 90; i <= 180; i++)
            {
                GL11.glVertex2d(x + (Math.sin((i * 3.141526D / 180)) * radius), y + (Math.cos((i * 3.141526D / 180)) * radius));
            }
        }
        else if (mode == 2)
        {
            for (int i = 90; i <= 180; i++)
            {
                GL11.glVertex2d(x + (Math.sin((i * 3.141526D / 180)) * (radius * -1)), y + (Math.cos((i * 3.141526D / 180)) * (radius * -1)));
            }
        }
        else if (mode == 3)
        {
            for (int i = 0; i <= 90; i++)
            {
                GL11.glVertex2d(x + (Math.sin((i * 3.141526D / 180)) * radius), y + (Math.cos((i * 3.141526D / 180)) * radius));
            }
        }
        else
        {
        }
        GL11.glEnd();
        enableDefaults();
    }

    /* ##### 2D LINE METHODS ##### */

    public static void drawTexture(float x, float y, float textureX, float textureY, float width, float height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, (y + height), 0.0D).tex((textureX * f), ((textureY + height) * f1)).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((textureX + width) * f), ((textureY + height) * f1)).endVertex();
        bufferbuilder.pos((x + width), y, 0.0D).tex(((textureX + width) * f), (textureY * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex((textureX * f), (textureY * f1)).endVertex();
        tessellator.draw();
    }

    public static void drawTexture(float x, float y, float width, float height, float u, float v, float t, float s)
    {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x + width, y, 0F).tex(t, v).endVertex();
        bufferbuilder.pos(x, y, 0F).tex(u, v).endVertex();
        bufferbuilder.pos(x, y + height, 0F).tex(u, s).endVertex();
        bufferbuilder.pos(x, y + height, 0F).tex(u, s).endVertex();
        bufferbuilder.pos(x + width, y + height, 0F).tex(t, s).endVertex();
        bufferbuilder.pos(x + width, y, 0F).tex(t, v).endVertex();
        tessellator.draw();
    }
}