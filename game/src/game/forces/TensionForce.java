package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.Logger;
import sps.core.RNG;

public class TensionForce extends Force {
    @Override
    public Atom forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj];
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        Logger.info("Incoming: " + bodyPart.getScale());
        bodyPart.setScale(bodyPart.getScale() - bodyPart.getScale() * (RNG.next(10, 25) / 100f));
        Logger.info("Outgoing: " + bodyPart.getScale());
    }
}
