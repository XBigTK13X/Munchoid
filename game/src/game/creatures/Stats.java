package game.creatures;

import game.forces.Force;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private static final int __baseStat = 10;

    private Map<Force, Integer> _stats;

    public Stats() {
        _stats = new HashMap<Force, Integer>();
        for (Force force : Force.values()) {
            _stats.put(force, __baseStat);
        }
    }

    public int get(Force force) {
        return _stats.get(force);
    }

    public void set(Force force, int value) {
        _stats.put(force, value);
    }
}
