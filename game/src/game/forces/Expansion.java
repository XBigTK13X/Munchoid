package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Expansion extends BaseForce {
    public Expansion(int magnitude) {
        super(magnitude, GameConfig.ExpansionScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        bodyPart.setScale(bodyPart.getScale() + RNG.next(getScaledMagnitude(), (int) ((getScaledMagnitude() + getPartScale(bodyPart)) * 1.1f)) / 100f);
    }
}
