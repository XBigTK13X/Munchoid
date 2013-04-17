package game.creatures;

import game.forces.Force;
import sps.core.RNG;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private static final int __baseStatMin = 5;
    private static final int __baseStatMax = 10;

    private Map<Force, Integer> _stats;

    public Stats() {
        _stats = new HashMap<Force, Integer>();
        for (Force force : Force.values()) {
            _stats.put(force, RNG.next(__baseStatMin, __baseStatMax));
        }
    }

    public int get(Force force) {
        return _stats.get(force);
    }

    public void set(Force force, int value) {
        _stats.put(force, value);
    }

    public Integer power() {
        int power = 0;
        for (Force force : Force.values()) {
            power += _stats.get(force);
        }
        return power;
    }
}
