package me.catmi.util;

public class StopWatchUtil {
    private static long previousMS;

    public StopWatchUtil () {
        this.reset();
    }

    public static boolean hasCompleted(long milliseconds) {
        return StopWatchUtil .getCurrentMS() - previousMS >= milliseconds;
    }

    public void reset() {
        previousMS = StopWatchUtil .getCurrentMS();
    }

    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
}
