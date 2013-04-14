package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Vaporize extends BaseForce {
    int _magnitude;

    public Vaporize() {
        _magnitude = RNG.next(10, 40);
    }

    @Override
    public Atom forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return RNG.percent(_magnitude) ? null : bodyPart.getAtoms()[ii][jj];
    }
}
