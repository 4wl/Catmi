package me.catmi.module.modules.render;


import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.module.modules.combat.AutoCrystal;
import me.catmi.module.modules.hud.HUD2;
import me.catmi.settings.Setting;
import me.catmi.util.MathUtil;
import me.catmi.util.RenderUtil4;
import me.catmi.util.font.FontUtils;
import me.catmi.util.font.OGCFONT;
import me.catmi.util.render.RenderUtil;
import me.catmi.util.render.RenderUtil2;
import me.catmi.util.world.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TargetHUD extends Module {
    public TargetHUD() {
        super("TargetHUD", Category.Render);
    }

    Setting.Integer x_;
    Setting.Integer y_;

    @Override
    public void setup() {
        x_ = registerInteger("X", "X", 2, 0, 500);
        y_ = registerInteger("Y", "Y", 2, 0, 500);
    }

    @Override
    public void onRender() {
        int x = this.x_.getValue();
        int y = this.y_.getValue();
        int width = 150;
        int height = 50;
        Entity target = AutoCrystal.renderEnt;
        if (target != null) {
            NetworkPlayerInfo networkPlayerInfo = mc.getConnection().getPlayerInfo(target.getUniqueID());

            final String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");

            final String playerName = "Name: " + StringUtils.stripControlCodes(target.getName());
            RenderUtil4.drawBorderedRect2(x, y, width, height, 0.5f, new Color(196, 67, 67, 91).getRGB(), new Color(0, 0, 0, 90).getRGB());
            RenderUtil4.drawRect2(x, y, 45, 45, new Color(80, 57, 57).getRGB());
            Catmi.fontRenderer2.drawStringWithShadow(playerName, x + 46.5, y + 4, -1);
            Catmi.fontRenderer2.drawStringWithShadow("Distance: " + MathUtil.round(mc.player.getDistance(target), 2), x + 46.5, y + 12, -1);
            Catmi.fontRenderer2.drawStringWithShadow(ping, x + 46.5, y + 28, new Color(0x84335B).getRGB());
            Catmi.fontRenderer2.drawStringWithShadow("Health: " + MathUtil.round(EntityUtil.totalHealth((EntityPlayer) target) / 2, 2), x + 46.5, y + 20, getHealthColor((EntityLivingBase) target));
            drawFace(x + 0.5, y + 0.5, 8, 8, 8, 8, 44, 44, 64, 64, (AbstractClientPlayer) target);
            RenderUtil4.drawBorderedRect2(x + 46, y + height - 10, 92, 8, 0.5f, new Color(0x5E762C2C, true).getRGB(), new Color(35, 35, 35).getRGB());
            float inc = 91 / 20;
            float end = inc * (EntityUtil.totalHealth((EntityPlayer) target) > 20 ? 20 : EntityUtil.totalHealth((EntityPlayer) target));
            RenderUtil4.drawRect2(x + 46.5f, y + height - 9.5f, end, 7, getHealthColor((EntityLivingBase) target));
        }
    }

    private void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            RenderUtil4.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception e) {
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
    }

    public int getPing(EntityPlayer player) {
        int ping = 0;
        try {
            ping = (int) MathUtil.clamp(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1.0f, 300.0f);
        } catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return ping;
    }

    public int getServerip(EntityPlayer player) {

        int server = player.maxHurtTime;


        return server;
    }
}
