package game;

import game.creatures.Stats;

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
    private static final int __petVarietyWeight = 100;
    private static final int __petPowerWeight = 25;

    private static final int __scoreMult = 13;

    private int _victories;
    private int _chomps;
    private int _petVariety;
    private int _petPower;

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
        return (__scoreMult) * (
                _victories * __victoryWeight +
                        _chomps * __chompWeight +
                        _petVariety * __petVarietyWeight +
                        _petPower * __petPowerWeight
        );
    }

    public String message() {
        return "Total score: " + total() + " point" + ((total() == 1) ? "" : "s");
    }

    public void setPlayerPetStats(Stats stats) {
        _petVariety = stats.possibleActiveForces();
        _petPower = stats.power();
    }
}
