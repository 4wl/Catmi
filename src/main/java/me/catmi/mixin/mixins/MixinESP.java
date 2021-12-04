package me.catmi.mixin.mixins;

import me.catmi.module.ModuleManager;
import me.catmi.module.modules.render.ESP;
import me.catmi.players.friends.Friends;
import me.catmi.util.OutlineUtils;
import me.catmi.util.render.RenderUtil;
import me.catmi.util.render.RenderUtil2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(RenderLivingBase.class)
public abstract class MixinESP<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected ModelBase mainModel;

    protected MixinESP() {
        super(null);
    }



    /**
     * @author
     */
    @Overwrite
    protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
        boolean isPlayer = entitylivingbaseIn instanceof EntityPlayer;

        if (!bindEntityTexture(entitylivingbaseIn)) {
            return;
        }


        Minecraft mc = Minecraft.getMinecraft();
        boolean fancyGraphics = mc.gameSettings.fancyGraphics;
        mc.gameSettings.fancyGraphics = false;

        float gamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100000F;
        if (ModuleManager.isModuleEnabled("ESP")) {
            switch (ESP.mode.getValue()) {
                case "WireFrame":
                    if (isPlayer) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glEnable(GL11.GL_LINE_SMOOTH);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        Color n = new Color(ESP.Red.getValue(), ESP.Green.getValue(), ESP.Blue.getValue());
                        if (Friends.isFriend(entitylivingbaseIn.getName())) {
                            n = new Color(ESP.Red2.getValue(), ESP.Green2.getValue(), ESP.Blue2.getValue(), 255);
                        }
                        RenderUtil2.color(n.getRGB());
                        GL11.glLineWidth((float) ESP.width.getValue());
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }

                case "OutLine":
                    boolean player = entitylivingbaseIn instanceof EntityPlayer && entitylivingbaseIn != Minecraft.getMinecraft().player;
                    if (player) {
                        Color n = new Color(ESP.Red.getValue(), ESP.Green.getValue(), ESP.Blue.getValue());
                        if (Friends.isFriend(entitylivingbaseIn.getName())) {
                            n = new Color(ESP.Red2.getValue(), ESP.Green2.getValue(), ESP.Blue2.getValue(), 255);
                        }
                        OutlineUtils.setColor(n);
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderOne((float) ESP.width.getValue());
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderTwo();
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderThree();
                        OutlineUtils.renderFour();
                        OutlineUtils.setColor(n);
                        mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                        break;

                    }
            }
        }


        mc.gameSettings.fancyGraphics = fancyGraphics;
        mc.gameSettings.gammaSetting = gamma;


        if (!ESP.mode.getValue().equalsIgnoreCase("Wireframe") || !ModuleManager.isModuleEnabled("ESP") || !isPlayer) {
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
        }

    }
}