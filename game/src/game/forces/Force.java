package game.forces;

import sps.core.RNG;

public enum Force {
    Abrasive,
    Expansion,
    Explosive,
    Slice,
    Contraction,
    Vaporize;

    private Force() {

    }

    public static BaseForce create(Force force) {
        switch (force) {
            case Abrasive:
                return new Abrasive();
            case Expansion:
                return new Expansion();
            case Explosive:
                return new Explosive();
            case Slice:
                return new Slice();
            case Contraction:
                return new Contraction();
            case Vaporize:
                return new Vaporize();
        }
        return null;
    }

    public static BaseForce createRandom() {
        return create(Force.values()[RNG.next(0, Force.values().length)]);
    }

    public static Force random() {
        return values()[RNG.next(0, values().length)];
    }
}
