
package micycle.polygonmorphing.tools;

public class Stoppuhr {
    private static long startTime = 0L;
    private static boolean running = false;

    public static void start() {
        running = true;
        startTime = System.currentTimeMillis();
    }

    public static long stop() {
        if (!running) {
            return 0L;
        }
        running = false;
        return System.currentTimeMillis() - startTime;
    }
}

