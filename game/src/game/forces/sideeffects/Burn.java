package game.forces.sideeffects;

import game.creatures.Creature;
import sps.util.CoolDown;

public class Burn extends SideEffect {
    private CoolDown _coolDown;
    private static final float __delaySeconds = 3f;
    private static final int __damage = 1;

    protected Burn() {
        super(SideEffectType.ModHealth);
        _coolDown = new CoolDown(__delaySeconds);
    }

    @Override
    public boolean ready() {
        return _coolDown.isCool();
    }

    @Override
    public float affect(Creature creature) {
        creature.addHealthOffset(-__damage);
        return 0;
    }
}
