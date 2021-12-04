package me.catmi.module.modules.render;

import me.catmi.module.Module;
import net.minecraft.util.ResourceLocation;


public class Hitmarker extends Module {
  public Hitmarker(){
      super("HitMarker",Category.Render);
  }
    private ResourceLocation getBox() {
        return new ResourceLocation("hitmarker.textures.gui:hitmarker.png");
    }

}
