package game.forces.sideeffects;

import game.creatures.Creature;

public class Growth extends SideEffect {
    private static final int __extraDamage = 2;

    public Growth() {
        super(SideEffectType.DamageCaused);
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public float affect(Creature creature) {
        return __extraDamage;
    }
}
