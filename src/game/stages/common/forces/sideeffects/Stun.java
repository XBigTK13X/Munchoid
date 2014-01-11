package game.stages.common.forces.sideeffects;

import game.stages.common.creatures.Creature;

public class Stun extends SideEffect {
    private static final float __coolDownDelaySeconds = .05f;

    public Stun() {
        super(SideEffectType.DelayNextAttack);
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public float affect(Creature creature) {
        creature.getCoolDown().delay(__coolDownDelaySeconds);
        use();
        return 0;
    }
}
