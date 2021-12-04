package me.catmi.util;

import java.awt.Color;

public class RainbowUtil {
    public static int y;

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.7f, 1.0f).getRGB();
    }

    public static Color rainbowColor(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.8f, 0.7f);
    }

    private static float FutureClientColorCalculation(float n, float n2, float n3) {
        if (n3 < 0.0f) {
            n3 += 1.0f;
        }
        if (n3 > 1.0f) {
            n3 -= 1.0f;
        }
        if (6.0f * n3 < 1.0f) {
            return n + (n2 - n) * 6.0f * n3;
        }
        if (2.0f * n3 < 1.0f) {
            return n2;
        }
        if (3.0f * n3 < 2.0f) {
            return n + (n2 - n) * 6.0f * (0.6666667f - n3);
        }
        return n;
    }

    public static Color ColorRainbowWithDefaultAlpha(float n, float n2, float n3) {
        return RainbowUtil.GetRainbowColor(n, n2, n3, 1.0f);
    }

    public static Color GetRainbowColor(float p_Hue, float p_Saturation, float p_Lightness, float p_Alpha) {
        if (p_Saturation < 0.0f || p_Saturation > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        }
        if (p_Lightness < 0.0f || p_Lightness > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
        }
        if (p_Alpha < 0.0f || p_Alpha > 1.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
        }
        p_Hue %= 360.0f;
        float n5 = (double)(p_Lightness /= 100.0f) < 0.0 ? p_Lightness * (1.0f + p_Saturation) : p_Lightness + (p_Saturation /= 100.0f) - p_Saturation * p_Lightness;
        p_Saturation = 2.0f * p_Lightness - n5;
        p_Lightness = Math.max(0.0f, RainbowUtil.FutureClientColorCalculation(p_Saturation, n5, (p_Hue /= 360.0f) + 0.33333334f));
        float max = Math.max(0.0f, RainbowUtil.FutureClientColorCalculation(p_Saturation, n5, p_Hue));
        p_Saturation = Math.max(0.0f, RainbowUtil.FutureClientColorCalculation(p_Saturation, n5, p_Hue - 0.33333334f));
        p_Lightness = Math.min(p_Lightness, 1.0f);
        float min = Math.min(max, 1.0f);
        p_Saturation = Math.min(p_Saturation, 1.0f);
        return new Color(p_Lightness, min, p_Saturation, p_Alpha);
    }
}
