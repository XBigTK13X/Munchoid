package game.forces.sideeffects;

import game.creatures.Creature;

public abstract class SideEffect {
    /*

Explosive
Burn
Health lost over time

Slice
Cripple
Increase wait time for next attack

Contraction
Quickening
Speed increase

Expansion
Burdon
Increased damage from

Vaporize
Ethereal
Decrease energy regen

Abrasive
Weak Flesh
Increased damage to on next hit

     */
    private SideEffectType _effect;
    private boolean _used;

    protected SideEffect(SideEffectType effect) {
        _effect = effect;
    }

    public SideEffectType getEffect() {
        return _effect;
    }

    public boolean isUsed() {
        return _used;
    }

    public void use() {
        _used = true;
    }

    public abstract boolean ready();

    public abstract float affect(Creature creature);
}
