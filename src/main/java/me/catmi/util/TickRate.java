package me.catmi.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;

public class TickRate {
    public static float TPS = 20.0f;
    public static TickRate INSTANCE;
    public static long lastUpdate;
    public static float[] tpsCounts;
    public static DecimalFormat format;

    public static void update(Packet packet) {
        float tps;
        if (!(packet instanceof SPacketTimeUpdate)) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (lastUpdate == -1L) {
            lastUpdate = currentTime;
            return;
        }
        long timeDiff = currentTime - lastUpdate;
        float tickTime = timeDiff / 20L;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        if ((tps = 1000.0f / tickTime) > 20.0f) {
            tps = 20.0f;
        }
        if (tpsCounts.length - 1 >= 0) {
            System.arraycopy(tpsCounts, 0, tpsCounts, 1, tpsCounts.length - 1);
        }
        TickRate.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f : tpsCounts) {
            total += (double)f;
        }
        if ((total /= (double)tpsCounts.length) > 20.0) {
            total = 20.0;
        }
        TPS = Float.parseFloat(format.format(total));
        lastUpdate = currentTime;
    }

    public static void reset() {
        Arrays.fill(tpsCounts, 20.0f);
        TPS = 20.0f;
    }

    static {
        lastUpdate = -1L;
        tpsCounts = new float[10];
        format = new DecimalFormat("##.0#");
    }
}