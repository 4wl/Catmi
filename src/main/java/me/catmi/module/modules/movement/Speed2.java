package me.catmi.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.catmi.event.events.PlayerMoveEvent;
import me.catmi.module.Module;
import me.catmi.settings.Setting;
import me.catmi.util.world.EntityUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Speed2
        extends Module {
    Setting.Mode mode;
    Setting.Integer acceleration;
    Setting.Integer specialMoveSpeed;
    Setting.Boolean limiter;
    Setting.Boolean limiter2;
    Setting.Boolean potion;
    Setting.Integer potionSpeed1;
    Setting.Integer potionSpeed2;
    private int stage;
    private int cooldownHops;
    private final int ticks = 0;
    private double moveSpeed;
    private double lastDist;

    public Speed2() {
        super("Speed2", Module.Category.Movement);
    }

    @Override
    public void setup() {
        this.acceleration = this.registerInteger("Accel", "Accel", 2149, 1000, 2500);
        this.specialMoveSpeed = this.registerInteger("Speed", "Speed", 100, 0, 150);
        this.limiter = this.registerBoolean("Limiter", "Limiter", true);
        this.limiter2 = this.registerBoolean("Limiter 2", "Limiter 2", true);
        this.potion = this.registerBoolean("Speed", "Speed", true);
        this.potionSpeed1 = this.registerInteger("Speed1", "Speed1", 130, 0, 150);
        this.potionSpeed2 = this.registerInteger("Speed2", "Speed2", 125, 0, 150);
    }

    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Minecraft.getMinecraft().player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    @Override
    public void onEnable() {
        if (Speed2.mc.player != null) {
            this.moveSpeed = Speed2.defaultSpeed();
        }
        this.lastDist = 0.0;
        this.stage = 2;
    }

    public  Listener<PlayerMoveEvent> listener = new Listener<>(event -> {
        if (Speed2.mc.player != null) {
            float moveForward = Speed2.mc.player.movementInput.moveForward;
            float moveStrafe = Speed2.mc.player.movementInput.moveStrafe;
            float rotationYaw = Speed2.mc.player.rotationYaw;
            if (this.limiter2.getValue() && Speed2.mc.player.onGround) {
                this.stage = 2;
            }
            if (this.limiter.getValue() && Speed2.round(Speed2.mc.player.posY - (double)((int)Speed2.mc.player.posY), 3) == Speed2.round(0.138, 3)) {
                EntityPlayerSP player = Speed2.mc.player;
                player.motionY -= 0.13;
                event.setY(event.getY() - 0.13);
                EntityPlayerSP player2 = Speed2.mc.player;
                player2.posY -= 0.13;
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = (double)this.getMultiplier() * this.getBaseMoveSpeed() - 0.01;
            } else if (this.stage == 2) {
                this.stage = 3;
                if (EntityUtil.isMoving()) {
                    Speed2.mc.player.motionY = 0.4;
                    event.setY(0.4);
                    if (this.cooldownHops > 0) {
                        --this.cooldownHops;
                    }
                    this.moveSpeed *= (double)this.acceleration.getValue() / 1000.0;
                }
            } else if (this.stage == 3) {
                this.stage = 4;
                double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            } else {
                if (Speed2.mc.world.getCollisionBoxes((Entity)Speed2.mc.player, Speed2.mc.player.getEntityBoundingBox().offset(0.0, Speed2.mc.player.motionY, 0.0)).size() > 0 || Speed2.mc.player.collidedVertically) {
                    this.stage = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            } else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                    moveStrafe = 0.0f;
                } else if (moveStrafe <= -1.0f) {
                    rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                } else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                event.setX((double)moveForward * this.moveSpeed * motionX + (double)moveStrafe * this.moveSpeed * motionZ);
                event.setZ((double)moveForward * this.moveSpeed * motionZ - (double)moveStrafe * this.moveSpeed * motionX);
            }
            Speed2.mc.player.stepHeight = 0.6f;
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
    });

    private float getMultiplier() {
        float baseSpeed = this.specialMoveSpeed.getValue();
        if (this.potion.getValue() && Speed2.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Speed2.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
            baseSpeed = amplifier >= 2 ? (float)this.potionSpeed2.getValue() : (float)this.potionSpeed1.getValue();
        }
        return baseSpeed / 100.0f;
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Speed2.mc.player != null && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Speed.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    @Override
    public String getHudInfo(){
        return    "[" + ChatFormatting.WHITE + "NCP" + ChatFormatting.GRAY + "]";
    }
}
