package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.RNG;

public class ExpansiveForce extends Force {
    @Override
    public Atom forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj];
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        bodyPart.setScale(bodyPart.getScale() + RNG.next(120, 150) / 100f);
    }
}
