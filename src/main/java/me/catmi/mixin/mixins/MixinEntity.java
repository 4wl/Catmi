package me.catmi.mixin.mixins;

import me.catmi.module.ModuleManager;
import me.catmi.module.modules.movement.PlayerTweaks;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class MixinEntity{

	@Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	public void velocity(Entity entity, double x, double y, double z){
		if (((PlayerTweaks)ModuleManager.getModuleByName("PlayerTweaks")).noPush.getValue() == false){
			entity.motionX += x;
			entity.motionY += y;
			entity.motionZ += z;
			entity.isAirBorne = true;
		}
	}
}

