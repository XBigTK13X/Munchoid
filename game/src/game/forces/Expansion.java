package game.forces;

import game.creatures.BodyPart;
import sps.core.RNG;

public class Expansion extends BaseForce {
    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        bodyPart.setScale(bodyPart.getScale() + RNG.next(103, 108) / 100f);
    }
}
