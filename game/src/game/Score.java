package game;

public class Score {
    private static Score __instance;

    public static Score get() {
        if (__instance == null) {
            reset();
        }
        return __instance;
    }

    public static void reset() {
        __instance = new Score();
    }

    private static final int __victoryWeight = 10;
    private static final int __chompWeight = 1;

    private int _victories;
    private int _chomps;

    private Score() {

    }

    public void addChomp() {
        _chomps++;
    }

    public void addVictory() {
        _victories++;
    }

    public int victories() {
        return _victories;
    }

    public int total() {
        return _victories * __victoryWeight + _chomps * __chompWeight;
    }

    public String message() {
        return "Total score: " + total() + " point" + ((total() == 1) ? "" : "s");
    }
}
