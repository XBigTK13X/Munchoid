package game.forces;

import game.creatures.BodyPart;
import sps.core.RNG;

public class Vaporize extends BaseForce {
    int _magnitude;

    public Vaporize() {
        _magnitude = RNG.next(10, 40);
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return RNG.percent(_magnitude) ? false : bodyPart.getAtoms()[ii][jj].isActive();
    }
}
