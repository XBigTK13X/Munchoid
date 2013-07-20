package game.creatures;

import game.GameConfig;
import game.forces.Force;
import sps.core.RNG;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    public static Stats createWeakling(Stats stats) {
        Stats result = new Stats();
        for (Force f : Force.values()) {
            result.set(f, 0);
        }
        Force strength = stats.randomEnabledForce();
        Force weakness = Force.beats(strength);
        result.set(weakness, stats.get(strength) / 2);
        return result;
    }

    private Map<Force, Integer> _stats;
    private Map<Force, Boolean> _enabled;

    public Stats() {
        _stats = new HashMap<Force, Integer>();
        _enabled = new HashMap<Force, Boolean>();
        for (Force force : Force.values()) {
            _stats.put(force, GameConfig.MinStat);
            _enabled.put(force, true);
        }
        for (Force force : Force.random(GameConfig.InitEnabledStats)) {
            _stats.put(force, RNG.next(GameConfig.MinStatInit, GameConfig.MaxStatInit));
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
        while (true) {
            force = Force.random();
            if (get(force) > GameConfig.DisableStat && isEnabled(force)) {
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

    public int possibleActiveForces() {
        int max = 0;
        for (Force force : Force.values()) {
            if (get(force) > GameConfig.DisableStat) {
                max++;
            }
        }
        return max;
    }
}
