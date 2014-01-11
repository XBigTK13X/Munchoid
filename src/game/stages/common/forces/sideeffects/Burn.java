package game.stages.common.forces.sideeffects;

import game.stages.common.creatures.Creature;
import sps.util.CoolDown;

public class Burn extends SideEffect {
    private CoolDown _coolDown;
    private static final float __delaySeconds = 3f;
    private static final int __damage = -1;

    public Burn() {
        super(SideEffectType.Health);
        _coolDown = new CoolDown(__delaySeconds);
    }

    @Override
    public boolean ready() {
        return _coolDown.updateAndCheck();
    }

    @Override
    public float affect(Creature creature) {
        creature.addHealthOffset(__damage);
        return 0;
    }
}
