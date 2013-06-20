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

    private static final int __victoryWeight = 11;
    private static final int __chompWeight = 3;
    private static final int __petVarietyWeight = 119;
    private static final int __petPowerWeight = 27;
    private static final int __healthRemainingWeight = 16;

    private static final int __scoreMult = 13;

    private int _victories;
    private int _chomps;
    private int _petVariety;
    private int _petPower;
    private int _healthRemaining;

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
                        _petPower * __petPowerWeight +
                        _healthRemaining * __healthRemainingWeight
        );
    }

    public String message() {
        return "Total score: " + total() + " point" + ((total() == 1) ? "" : "s");
    }

    public void setPlayerPetStats(Stats stats) {
        _petVariety = stats.possibleActiveForces();
        _petPower = stats.power();
    }

    public void addHealthRemaining(float percentHealth) {
        _healthRemaining += percentHealth * 100;
    }
}
