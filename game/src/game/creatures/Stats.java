package game.creatures;

import game.GameConfig;
import game.forces.Force;
import sps.core.RNG;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    private Map<Force, Integer> _stats;

    public Stats() {
        _stats = new HashMap<Force, Integer>();
        for (Force force : Force.values()) {
            _stats.put(force, 0);
        }
        for (Force force : Force.random(1)) {
            _stats.put(force, RNG.next(GameConfig.BaseStatStartMin, GameConfig.BaseStatStartMax));
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

    public Force nonZeroForce() {
        Force force;
        while (true) {
            force = Force.random();
            if (get(force) > 0) {
                return force;
            }
        }
    }
}
