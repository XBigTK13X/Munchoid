package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Contraction extends BaseForce {
    public Contraction(int magnitude) {
        super(magnitude, GameConfig.ContractionScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        bodyPart.setScale(bodyPart.getScale() - bodyPart.getScale() * (RNG.next(getMagnitude(), getScaledMagnitude() + getPartScale(bodyPart)) / 100f));
    }
}
