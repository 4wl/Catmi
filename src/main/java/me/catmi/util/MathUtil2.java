package me.catmi.util;

public class MathUtil2 {

        public static double distance(float x, float y, float x1, float y1) {
            return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
        }

}
