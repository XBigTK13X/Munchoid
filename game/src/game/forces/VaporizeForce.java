package game.forces;

import game.creatures.Atom;
import sps.core.RNG;

public class VaporizeForce extends Force {
    int _magnitude;

    public VaporizeForce() {
        _magnitude = RNG.next(10, 40);
    }

    @Override
    public Atom forceSpecifics(Atom[][] atoms, int ii, int jj) {
        return RNG.percent(_magnitude) ? null : atoms[ii][jj];
    }
}
