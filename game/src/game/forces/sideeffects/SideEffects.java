package game.forces.sideeffects;

import game.creatures.Creature;

import java.util.ArrayList;
import java.util.List;

public class SideEffects {
    private List<SideEffect> _effects;
    private Creature _creature;

    public SideEffects(Creature creature) {
        _creature = creature;
        _effects = new ArrayList<SideEffect>();
    }

    public float act(SideEffectType effect) {
        float result = 0;
        for (SideEffect e : _effects) {
            if (e.getEffect() == effect && e.ready() && !e.isUsed()) {
                result += e.affect(_creature);
            }
        }
        return result;
    }

    public int getCount(SideEffectType effect) {
        int result = 0;
        for (SideEffect e : _effects) {
            if (e.getEffect() == effect) {
                result++;
            }
        }
        return result;
    }

    public void update() {
        for (int ii = 0; ii < _effects.size(); ii++) {
            if (_effects.get(ii).isUsed()) {
                _effects.remove(ii);
                ii--;
            }
        }
    }

    public void add(SideEffect sideEffect) {
        _effects.add(sideEffect);
    }
}
