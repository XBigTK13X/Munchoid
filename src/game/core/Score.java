package game.core;

import game.arena.Player;
import game.creatures.Creature;
import game.creatures.Stats;
import sps.entities.EntityManager;
import sps.util.JSON;

import java.text.NumberFormat;
import java.util.Locale;

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

    public static void set(Score score) {
        __instance = score;
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

    private int _acceptedMerges;
    private int _rejectedMerges;

    private Stats _petStats;

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

    public Stats petStats() {
        if (_petStats == null) {
            Player p = (Player) EntityManager.get().getPlayer();
            if (p == null) {
                return new Stats();
            }
            Creature c = p.getPet();
            _petStats = c.getStats();
            setPlayerPetStats(_petStats);
        }
        return _petStats;
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

    public void addMergeAccept() {
        _acceptedMerges++;
    }

    public void addMergeReject() {
        _rejectedMerges++;
    }

    public String message() {
        NumberFormat dollars = NumberFormat.getCurrencyInstance(Locale.US);
        return "Reward from your people: " + dollars.format(total());
    }

    public String json() {
        return "\"score\":{" +
                JSON.delimit(
                        JSON.pad("total", total()),
                        JSON.pad("victories", _victories),
                        JSON.pad("chomps", _chomps),
                        JSON.pad("healthRemaining", _healthRemaining),
                        JSON.pad("acceptedMerges", _acceptedMerges),
                        JSON.pad("rejectedMerges", _rejectedMerges)) +
                "}";
    }

    public void setPlayerPetStats(Stats stats) {
        _petVariety = stats.possibleActiveForces();
        _petPower = stats.power();
        _petStats = stats;
    }

    public void addHealthRemaining(float percentHealth) {
        _healthRemaining += percentHealth;
    }
}
