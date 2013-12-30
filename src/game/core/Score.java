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

    private static final int __scoreMult = 13;

    private static final int __victoryWeight = 11;
    private static final int __chompWeight = 3;
    private static final int __petVarietyWeight = 119;
    private static final int __petPowerWeight = 27;
    private static final int __healthRemainingWeight = 16;
    private static final int __tournamentWinWeight = 3500;
    private static final int __tournamentLossWeight = -1500;

    private int _victories;
    private int _chomps;
    private int _petVariety;
    private int _petPower;
    private int _healthRemaining;
    private int _tournamentWins;
    private int _tournamentLosses;

    private int _acceptedMerges;
    private int _rejectedMerges;

    private Stats _petStats;

    private Score() {

    }

    public void set(int victories, int chomps, int petVariety, int petPower, int healthRemaining, int acceptedMerges, int rejectedMerges, int tournamentWins, int tournamentLosses) {
        _victories = victories;
        _chomps = chomps;
        _petVariety = petVariety;
        _petPower = petPower;
        _healthRemaining = healthRemaining;
        _acceptedMerges = acceptedMerges;
        _rejectedMerges = rejectedMerges;
        _tournamentLosses = tournamentLosses;
        _tournamentWins = tournamentWins;
    }

    public void addChomp() {
        _chomps++;
    }

    public void addTournyWin() {
        _tournamentWins++;
    }

    public void addTournyLoss() {
        _tournamentLosses++;
    }

    public void addVictory() {
        _victories++;
    }

    public int victories() {
        return _victories;
    }

    public int chomps() {
        return _chomps;
    }

    public int petVariety() {
        return _petVariety;
    }

    public int petPower() {
        return _petPower;
    }

    public int healthRemaining() {
        return _healthRemaining;
    }

    public int acceptedMerges() {
        return _acceptedMerges;
    }

    public int rejectedMerges() {
        return _rejectedMerges;
    }

    public int tournamentWins() {
        return _tournamentWins;
    }

    public int tournamentLosses() {
        return _tournamentLosses;
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
                        _healthRemaining * __healthRemainingWeight +
                        _tournamentWins * __tournamentWinWeight +
                        _tournamentLosses * __tournamentLossWeight
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
                        JSON.pad("rejectedMerges", _rejectedMerges),
                        JSON.pad("tournamentWins", _tournamentWins),
                        JSON.pad("tournamentLosses", _tournamentLosses)) +
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
