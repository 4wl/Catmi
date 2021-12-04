package me.catmi.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class RotationManager {
    private float yaw;

    private float pitch;
    public int getDirection4D() {
        return RotationUtil.getDirection4D();
    }

    public String getDirection4D(boolean northRed) {
        return RotationUtil.getDirection4D(northRed);
    }
}
