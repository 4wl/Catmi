package me.catmi.module.modules.combat;

import me.catmi.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AutoClicker
   extends Module
       {
   private long last = 0L;

   public AutoClicker() {
     super("AutoClicker", Category.Combat);
      }

     public void onPreUpdate() {
         Vec3d posVec = this.mc.player.getPositionVector();
       RayTraceResult result = this.mc.world.rayTraceBlocks(posVec, posVec.add(5.0D, 0.0D, 0.0D), false, true, false);

         if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY &&
          System.currentTimeMillis() - this.last > 50L) {
           this.mc.playerController.attackEntity((EntityPlayer)this.mc.player, result.entityHit);
        this.last = System.currentTimeMillis();
            }
        }
    }
