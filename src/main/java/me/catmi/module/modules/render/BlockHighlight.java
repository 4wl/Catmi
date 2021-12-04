package me.catmi.module.modules.render;

import me.catmi.event.events.RenderEvent;
import me.catmi.settings.Setting;
import me.catmi.util.render.CatmiTessellator;
import me.catmi.util.world.GeometryMasks;
import me.catmi.util.CMColor;
import me.catmi.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;

public class BlockHighlight extends Module{
	public BlockHighlight(){
		super("BlockHighlight", Category.Render);
	}
	
	Setting.Integer w;
	Setting.Boolean shade;
	Setting.ColorSetting color;

	public void setup() {
		shade = registerBoolean("Fill", "Fill", false);
		w = registerInteger("Width", "Width", 2, 1, 10);
		color = registerColor("Color","Color");
	}

	public void onWorldRender(RenderEvent event) {
		RayTraceResult ray = mc.objectMouseOver;
		AxisAlignedBB bb;
		BlockPos pos;
		CMColor c2=new CMColor(color.getValue(),50);
		if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
			pos = ray.getBlockPos();
			bb = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos);
			if (bb != null && pos != null && mc.world.getBlockState(pos).getMaterial() != Material.AIR) {
				CatmiTessellator.prepareGL();
				CatmiTessellator.drawBoundingBox(bb, w.getValue(), color.getValue());
				CatmiTessellator.releaseGL();
				if (shade.getValue()) {
					CatmiTessellator.prepare(GL11.GL_QUADS);
					CatmiTessellator.drawBox(bb, c2, GeometryMasks.Quad.ALL);
					CatmiTessellator.release();
				}
			}
		}
	}
}
