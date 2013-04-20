package game.forces;

import game.creatures.BodyPart;
import sps.core.RNG;

public class Contraction extends BaseForce {
    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        bodyPart.setScale(bodyPart.getScale() - bodyPart.getScale() * (RNG.next(10, 25) / 100f));
    }
}
