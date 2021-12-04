package me.catmi.module.modules.render;

import me.catmi.event.events.RenderEvent;
import me.catmi.settings.Setting;
import me.catmi.util.render.CatmiTessellator;
import me.catmi.util.world.GeometryMasks;
import me.catmi.util.CMColor;
import me.catmi.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.item.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class EntityESP extends Module {
    public EntityESP() {super("EntityESP", Category.Render);}

    public void setup() {
        ArrayList<String> Modes = new ArrayList<>();
        Modes.add("Box");
        Modes.add("Outline");
        Modes.add("Glow");
        exp = registerBoolean("Exp Bottles", "ExpBottles", false);
        epearls = registerBoolean("Ender Pearls", "EnderPearls", false);
        crystals = registerBoolean("Crystals", "Crystals", false);
        items = registerBoolean("Items", "Items", false);
        orbs = registerBoolean("Exp Orbs", "ExpOrbs", false);
        renderMode = registerMode("Mode", "Mode", Modes, "Box");
		width=registerInteger("Width","Width",1,1,10);
		color=registerColor("Color","Color");
    }

    Setting.Mode renderMode;
    Setting.Boolean exp;
    Setting.Boolean epearls;
    Setting.Boolean items;
    Setting.Boolean orbs;
    Setting.Boolean crystals;
	Setting.Integer width;
	Setting.ColorSetting color;

    public void onWorldRender(RenderEvent event) {
		CMColor rgbColor=color.getValue();
        CMColor c=new CMColor(rgbColor,50);
		CMColor c2=new CMColor(rgbColor,255);
		boolean drawBox=renderMode.getValue().equalsIgnoreCase("Box");
		boolean drawOutline=renderMode.getValue().equalsIgnoreCase("Box") || renderMode.getValue().equalsIgnoreCase("Outline");
		boolean drawGlow=renderMode.getValue().equalsIgnoreCase("Glow");
		mc.world.loadedEntityList.stream()
				.filter(entity -> entity != mc.player)
				.forEach(e -> {
					boolean drawThisThing=false;
					if (exp.getValue() && e instanceof EntityExpBottle) drawThisThing=true;
					else if (epearls.getValue() && e instanceof EntityEnderPearl) drawThisThing=true;
					else if (crystals.getValue() && e instanceof EntityEnderCrystal) drawThisThing=true;
					else if (items.getValue() && e instanceof EntityItem) drawThisThing=true;
					else if (orbs.getValue() && e instanceof EntityXPOrb) drawThisThing=true;
					if (drawThisThing) {
						if (drawBox) {
							CatmiTessellator.prepare(GL11.GL_QUADS);
							CatmiTessellator.drawBox(e.getRenderBoundingBox(), c, GeometryMasks.Quad.ALL);
							CatmiTessellator.release();
						}
						if (drawOutline) {
							CatmiTessellator.prepareGL();
							CatmiTessellator.drawBoundingBox(e.getRenderBoundingBox(), width.getValue(), c2);
							CatmiTessellator.releaseGL();
						}
						if (drawGlow) e.setGlowing(true);
					}
					CatmiTessellator.releaseGL();
				});
	}

	public void onUpdate () {
		mc.world.loadedEntityList.stream()
				.filter(e -> e != mc.player)
				.forEach(e -> {
					if (renderMode.getValue().equalsIgnoreCase("Glow") == false) {
						if (e instanceof EntityExpBottle) {
							e.setGlowing(false);
						}
						if (e instanceof EntityEnderPearl) {
							e.setGlowing(false);
						}
						if (e instanceof EntityEnderCrystal) {
							e.setGlowing(false);
						}
						if (e instanceof EntityItem) {
							e.setGlowing(false);
						}
						if (e instanceof EntityXPOrb) {
							e.setGlowing(false);
						}
					}
					if (exp.getValue() == false && e instanceof EntityExpBottle) {
						e.setGlowing(false);
					}
					if (epearls.getValue() == false && e instanceof EntityEnderPearl) {
						e.setGlowing(false);
					}
					if (crystals.getValue() == false && e instanceof EntityEnderCrystal) {
						e.setGlowing(false);
					}
					if (items.getValue() == false && e instanceof EntityItem) {
						e.setGlowing(false);
					}
					if (orbs.getValue() == false && e instanceof EntityXPOrb) {
						e.setGlowing(false);
					}
				});
	}

    public void onDisable(){
        if (renderMode.getValue().equalsIgnoreCase("Glow")) {
            mc.world.loadedEntityList.stream()
                    .filter(e -> e != mc.player)
                    .forEach(e -> {
                        if (e instanceof EntityExpBottle) {
                            e.setGlowing(false);
                        }
                        if (e instanceof EntityEnderPearl) {
                            e.setGlowing(false);
                        }
                        if (e instanceof EntityEnderCrystal) {
                            e.setGlowing(false);
                        }
                        if (e instanceof EntityItem) {
                            e.setGlowing(false);
                        }
                        if (e instanceof EntityXPOrb) {
                            e.setGlowing(false);
                        }
                    });
        }
    }

    public String getHudInfo() {
        String t = "";
        if(renderMode.getValue().equalsIgnoreCase("Box")) {
            t = "[" + ChatFormatting.WHITE + "Box" + ChatFormatting.WHITE + "]";
        }
        if (renderMode.getValue().equalsIgnoreCase("Outline")) {
            t = "[" + ChatFormatting.WHITE + "Outline" + ChatFormatting.WHITE + "]";
        }
        if (renderMode.getValue().equalsIgnoreCase("Glow")) {
            t = "[" + ChatFormatting.WHITE + "Glow" + ChatFormatting.WHITE + "]";
        }
        return t;
    }
}
