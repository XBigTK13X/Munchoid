package game.stages.common.forces.sideeffects;

import game.stages.common.creatures.Creature;

public class Ethereal extends SideEffect {
    private static final float __energyRegenDelaySeconds = .5f;

    public Ethereal() {
        super(SideEffectType.EnergyRegenRate);
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public float affect(Creature creature) {
        return __energyRegenDelaySeconds;
    }
}
