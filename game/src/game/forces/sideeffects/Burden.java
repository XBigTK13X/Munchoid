package game.forces.sideeffects;

import game.creatures.Creature;

public class Burden extends SideEffect {
    private static final int __extraDamage = 2;

    public Burden() {
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
