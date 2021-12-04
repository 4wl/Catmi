package me.catmi.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class MathUtil {
    public static double round(double value, int places) {
        if (places < 0) {
            return value;
        }
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static double map(double value, double a, double b, double c, double d) {
        value = (value - a) / (b - a);
        return c + value * (d - c);
    }

    public static double getDistance(Vec3d pos, double x, double y, double z) {
        double deltaX = pos.x - x;
        double deltaY = pos.y - y;
        double deltaZ = pos.z - z;
        return MathHelper.sqrt((double)(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }
}
