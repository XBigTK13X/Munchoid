package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Contraction extends BaseForce {
    public Contraction(int magnitude) {
        super(magnitude, GameConfig.ContractionScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        part.setScale(part.getScale() - part.getScale() * (RNG.next(getMagnitude(), getScaledMagnitude() + getPartScale(part)) / 100f));
    }

    @Override
    public void animate(BodyPart part) {
    }
}
