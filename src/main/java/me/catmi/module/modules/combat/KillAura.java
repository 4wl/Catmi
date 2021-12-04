package me.catmi.module.modules.combat;

import me.catmi.event.events.PacketEvent;
import me.catmi.players.friends.Friends;
import me.catmi.settings.Setting;
import me.catmi.Catmi;
import me.catmi.module.Module;
import me.catmi.module.ModuleManager;
import me.catmi.util.MathUtil;
import me.catmi.util.ReflectionFields;
import me.catmi.util.Wrapper;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module{
	public KillAura(){
		super("KillAura", Category.Combat);
	}

	private Setting.Boolean swordOnly;
	private Setting.Boolean caCheck;
	private Setting.Boolean criticals;
	public static float yaw;
	public static float pitch;
	private Setting.Double range;
	public int ticks;
	protected long lastMS = -1L;
	private long currentMS = 0L;
	private boolean cancel;
	private int cancelled;

	public void setup(){
		range = registerDouble("Range", "Range", 5,0,10);
		swordOnly = registerBoolean("Sword Only", "SwordOnly",true);
		criticals = registerBoolean("Criticals", "Criticals",true);
		caCheck = registerBoolean("AC Check", "ACCheck",false);
	}

	private boolean isAttacking = false;

	public void onUpdate(){
		if (mc.player == null || mc.player.isDead) return;
		List<Entity> targets = mc.world.loadedEntityList.stream()
				.filter(entity -> entity != mc.player)
				.filter(entity -> mc.player.getDistance(entity) <= range.getValue())
				.filter(entity -> !entity.isDead)
				.filter(entity -> entity instanceof EntityPlayer)
				.filter(entity -> ((EntityPlayer) entity).getHealth() > 0)
				.filter(entity -> !Friends.isFriend(entity.getName()))
				.sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
				.collect(Collectors.toList());

		targets.forEach(target -> {
			if (swordOnly.getValue())
				if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return;

			if (caCheck.getValue())
				if (((AutoCrystal) ModuleManager.getModuleByName("AutoCrystal")).isActive) return;
			faceTarget(target, Float.MAX_VALUE, Float.MAX_VALUE);
			Wrapper.mc.player.rotationPitch += 9.0E-4F;
			attack(target);
		});
	}

	@EventHandler
	private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
		if (event.getPacket() instanceof CPacketUseEntity){
			if (criticals.getValue() && ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround && isAttacking){
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
			}
		}
		if (event.getPacket() instanceof CPacketPlayer) {
			CPacketPlayer look = (CPacketPlayer) event.getPacket();
			ReflectionFields.setCPacketPlayerYaw(look, yaw);
			ReflectionFields.setCPacketPlayerPitch(look, pitch);
		}
		if (((event.getPacket() instanceof CPacketPlayer)) && (this.cancel) && (this.cancelled < 2)) {
			event.cancel();
			this.cancelled += 1;
		} else if (this.cancelled == 2) {
			this.cancelled = 0;
			this.cancel = false;
		}
	});


	public void onEnable(){
		Catmi.EVENT_BUS.subscribe(this);
	}

	public void onDisable(){
		Catmi.EVENT_BUS.unsubscribe(this);
		yaw = Wrapper.mc.player.rotationYaw;
		pitch = Wrapper.mc.player.rotationPitch;
	}

	public void attack(Entity e){
		updateMS();

		faceTarget(e, Float.MAX_VALUE, Float.MAX_VALUE);
		Wrapper.mc.player.rotationPitch += 9.0E-4F;

		if (Wrapper.mc.player.getCooledAttackStrength(0.0F) == 1.0F) {
			faceTarget(e, Float.MAX_VALUE, Float.MAX_VALUE);
			Wrapper.mc.player.rotationPitch += 9.0E-4F;
			Wrapper.mc.playerController.attackEntity(Wrapper.mc.player, e);
			Wrapper.mc.player.swingArm(EnumHand.MAIN_HAND);
		}

		faceTarget(e, Float.MAX_VALUE, Float.MAX_VALUE);
		Wrapper.mc.player.rotationPitch += 9.0E-4F;

	}
	public void faceTarget(Entity target, float p_70625_2_, float p_70625_3_) {
		double var4 = target.posX - Wrapper.mc.player.posX;
		double var5 = target.posZ - Wrapper.mc.player.posZ;
		double var7;
		if ((target instanceof EntityLivingBase)) {
			EntityLivingBase var6 = (EntityLivingBase) target;
			var7 = var6.posY + var6.getEyeHeight() - (Wrapper.mc.player.posY + Wrapper.mc.player.getEyeHeight());
		} else {
			var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D
					- (Wrapper.mc.player.posY + Wrapper.mc.player.getEyeHeight());
		}
		double var8 = MathHelper.sqrt(var4 * var4 + var5 * var5);
		float var9 = (float) (Math.atan2(var5, var4) * 180.0D / 3.141592653589793D) - 90.0F;
		float var10 = (float) -(Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.5F : 0.0F), var8) * 180.0D
				/ 3.141592653589793D);
		pitch = changeRotation(Wrapper.mc.player.rotationPitch, var10, p_70625_3_);
		yaw = changeRotation(Wrapper.mc.player.rotationYaw, var9, p_70625_2_);
		Wrapper.mc.player.rotationYawHead = yaw;
	}
	public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
		float var4 = MathHelper.wrapDegrees(p_70663_2_ - p_70663_1_);
		if (var4 > p_70663_3_) {
			var4 = p_70663_3_;
		}
		if (var4 < -p_70663_3_) {
			var4 = -p_70663_3_;
		}
		return p_70663_1_ + var4;
	}
	public final void updateLastMS() {
		this.lastMS = System.currentTimeMillis();
	}

	public final boolean hasTimePassedM(double d) {
		return this.currentMS >= this.lastMS + d;
	}

	public final void updateMS() {
		this.currentMS = System.currentTimeMillis();
	}
}
