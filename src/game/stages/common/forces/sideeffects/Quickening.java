package game.stages.common.forces.sideeffects;

import game.stages.common.creatures.Creature;

public class Quickening extends SideEffect {
    private static final float __coolDownImpact = -.05f;

    public Quickening() {
        super(SideEffectType.CoolDown);
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public float affect(Creature creature) {
        return __coolDownImpact;
    }
}
