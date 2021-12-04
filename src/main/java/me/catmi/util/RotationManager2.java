package me.catmi.util;

import net.minecraft.client.Minecraft;

public class RotationManager2 {

    private float yaw;
    private float pitch;

    public void updateRotations() {
        this.yaw = Minecraft.getMinecraft().player.rotationYaw;
        this.pitch = Minecraft.getMinecraft().player.rotationPitch;
    }

    public void restoreRotations() {
        Minecraft.getMinecraft().player.rotationYaw = yaw;
        Minecraft.getMinecraft().player.rotationYawHead = yaw;
        Minecraft.getMinecraft().player.rotationPitch = pitch;
    }

    public void setPlayerRotations(float yaw, float pitch) {
        Minecraft.getMinecraft().player.rotationYaw = yaw;
        Minecraft.getMinecraft().player.rotationYawHead = yaw;
        Minecraft.getMinecraft().player.rotationPitch = pitch;
    }

    public void setPlayerYaw(float yaw) {
        Minecraft.getMinecraft().player.rotationYaw = yaw;
        Minecraft.getMinecraft().player.rotationYawHead = yaw;
    }

    public void setPlayerPitch(float pitch) {
        Minecraft.getMinecraft().player.rotationPitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
