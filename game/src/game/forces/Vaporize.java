package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Vaporize extends BaseForce {
    public Vaporize(int magnitude) {
        super(magnitude, GameConfig.VaporizeScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        return RNG.percent(getScaledMagnitude()) ? false : bodyPart.getAtoms()[ii][jj].isActive();
    }
}
