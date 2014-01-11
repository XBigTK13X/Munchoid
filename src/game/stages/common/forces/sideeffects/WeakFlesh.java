package game.stages.common.forces.sideeffects;

import game.stages.common.creatures.Creature;

public class WeakFlesh extends SideEffect {
    private static final int __extraDamage = 2;

    public WeakFlesh() {
        super(SideEffectType.DamageTaken);
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public float affect(Creature creature) {
        use();
        return __extraDamage;
    }
}
