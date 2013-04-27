package game.forces;

import game.creatures.BodyPart;
import sps.core.RNG;

public class Expansion extends BaseForce {
    public Expansion(int magnitude) {
        super(magnitude, BaseForce.NoScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        bodyPart.setScale(bodyPart.getScale() + RNG.next(getMagnitude(), (int) (getMagnitude() * 1.1f)) / 100f);
    }
}
