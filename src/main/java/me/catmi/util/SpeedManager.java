package me.catmi.util;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class SpeedManager {
    public double firstJumpSpeed = 0.0D;

    public double lastJumpSpeed = 0.0D;

    public double percentJumpSpeedChanged = 0.0D;

    public double jumpSpeedChanged = 0.0D;

    public static boolean didJumpThisTick = false;

    public static boolean isJumping = false;

    public boolean didJumpLastTick = false;

    public long jumpInfoStartTime = 0L;

    public boolean wasFirstJump = true;

    public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0D;

    public double speedometerCurrentSpeed = 0.0D;

    public HashMap<EntityPlayer, Double> playerSpeeds = new HashMap<>();

    private int distancer = 20;

    public static void setDidJumpThisTick(boolean val) {
        didJumpThisTick = val;
    }

    public static void setIsJumping(boolean val) {
        isJumping = val;
    }

    public float lastJumpInfoTimeRemaining() {
        return (float)(Minecraft.getSystemTime() - this.jumpInfoStartTime) / 1000.0F;
    }

    public void updateValues() {
        double distTraveledLastTickX = Wrapper.getMinecraft().player.posX - Wrapper.getMinecraft().player.prevPosX;
        double distTraveledLastTickZ = Wrapper.getMinecraft().player.posZ - Wrapper.getMinecraft().player.prevPosZ;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (didJumpThisTick && (!Wrapper.getMinecraft().player.onGround || isJumping)) {
            if (didJumpThisTick && !this.didJumpLastTick) {
                this.wasFirstJump = (this.lastJumpSpeed == 0.0D);
                this.percentJumpSpeedChanged = (this.speedometerCurrentSpeed != 0.0D) ? (this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0D) : -1.0D;
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = Minecraft.getSystemTime();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0D;
            }
            this.didJumpLastTick = didJumpThisTick;
        } else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0D;
        }
    }



    public double getPlayerSpeed(EntityPlayer player) {
        if (this.playerSpeeds.get(player) == null)
            return 0.0D;
        return turnIntoKpH(((Double)this.playerSpeeds.get(player)).doubleValue());
    }

    public double turnIntoKpH(double input) {
        return MathHelper.sqrt(input) * 71.2729367892D;
    }

    public double getSpeedKpH() {
        double speedometerkphdouble = turnIntoKpH(this.speedometerCurrentSpeed);
        speedometerkphdouble = Math.round(10.0D * speedometerkphdouble) / 10.0D;
        return speedometerkphdouble;
    }

    public double getSpeedMpS() {
        double speedometerMpsdouble = turnIntoKpH(this.speedometerCurrentSpeed) / 3.6D;
        speedometerMpsdouble = Math.round(10.0D * speedometerMpsdouble) / 10.0D;
        return speedometerMpsdouble;
    }
}
