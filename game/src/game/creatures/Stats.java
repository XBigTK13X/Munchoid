package game.creatures;

import game.GameConfig;
import game.forces.Force;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stats {
    public static Stats createWeakling(Stats stats) {
        Stats result = new Stats();
        for (Force f : Force.values()) {
            result.set(f, 0);
            result.setEnabled(f, false);
        }
        Force strength = stats.randomEnabledForce();
        Force weakness = Force.beats(strength);
        result.set(weakness, stats.get(strength) / 2);
        result.setEnabled(weakness, true);
        return result;
    }

    private Map<Force, Integer> _stats;
    private Map<Force, Boolean> _enabled;

    public Stats() {
        _stats = new HashMap<Force, Integer>();
        _enabled = new HashMap<Force, Boolean>();
        for (Force force : Force.values()) {
            _stats.put(force, GameConfig.MinStat);
        }
        for (Force force : Force.random(GameConfig.InitEnabledStats)) {
            _stats.put(force, RNG.next(GameConfig.MinStatInit, GameConfig.MaxStatInit));
        }
        for (Force force : Force.values()) {
            setEnabled(force, canBeEnabled(force));
        }
    }

    public Stats(Stats copy) {
        this();
        for (Force force : Force.values()) {
            set(force, copy.get(force));
            setEnabled(force, canBeEnabled(force));
        }
    }

    public int get(Force force) {
        return _stats.get(force);
    }

    public void set(Force force, int value) {
        if (value > GameConfig.MaxStat) {
            value = GameConfig.MaxStat;
        }
        _stats.put(force, value);
    }

    public Integer power() {
        int power = 0;
        for (Force force : Force.values()) {
            power += _stats.get(force);
        }
        return power;
    }

    public Force randomEnabledForce() {
        Force force;
        if (maxEnabled() == 0) {
            throw new RuntimeException("No forces can be selected.");
        }
        while (true) {
            force = Force.random();
            if (canBeEnabled(force) && isEnabled(force)) {
                return force;
            }
        }
    }

    public boolean isEnabled(Force force) {
        return _enabled.get(force);
    }

    public void toggleEnabled(Force force) {
        _enabled.put(force, !_enabled.get(force));
    }

    public int maxEnabled() {
        return Math.min(possibleActiveForces(), GameConfig.MaxForcesEnabled);
    }

    public int enabledCount() {
        int count = 0;
        for (Force force : Force.values()) {
            count += _enabled.get(force) ? 1 : 0;
        }
        return count;
    }

    public void setEnabled(Force force, boolean enabled) {
        _enabled.put(force, enabled);
    }

    public boolean canBeEnabled(Force force) {
        return get(force) > GameConfig.DisableStat;
    }

    public Force randomEnablePossible() {
        List<Force> possible = new ArrayList<Force>();
        for (Force f : Force.values()) {
            if (canBeEnabled(f)) {
                possible.add(f);
            }
        }
        return RNG.pick(possible);
    }

    public int possibleActiveForces() {
        int max = 0;
        for (Force force : Force.values()) {
            if (canBeEnabled(force)) {
                max++;
            }
        }
        return max;
    }

    public void activateRandom() {
        int remainingPicks = maxEnabled();
        List<Force> forces = new ArrayList<Force>();
        for (Force f : Force.values()) {
            if (canBeEnabled(f)) {
                forces.add(f);
            }
            setEnabled(f, false);
        }
        while (remainingPicks > 0) {
            int pick = RNG.next(forces.size());
            setEnabled(forces.get(pick), true);
            forces.remove(pick);
            remainingPicks--;
        }
    }

    public void grow() {
        Force f = Force.random();
        int base = get(f);
        base += Math.max(1, base * (GameConfig.NaturalStatGrowthPercent / 100f));
        set(f, base);
        activateRandom();
    }

    public String json() {
        int fC = 0;
        String stats = "\"stats\":{";
        for (Force f : Force.values()) {
            stats += "\"" + f + "\"" + ":{\"strength\":\"" + get(f) + "\",\"enabled\":\"" + isEnabled(f) + "\"}";
            if (fC++ < Force.values().length - 1) {
                stats += ",";
            }
        }
        stats += "}";
        return stats;
    }
}