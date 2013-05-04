package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;

public class Explosive extends BaseForce {
    private Point2 _epicenter;

    public Explosive(int magnitude) {
        super(magnitude, GameConfig.ExplosiveScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {


        int adjustedMag = (getMagnitude() + RNG.next(-getMagnitude(), getMagnitude())) * (getScale() + getPartScale(bodyPart));
        int adjustedMagSquared = adjustedMag * adjustedMag;
        if (HitTest.getDistanceSquare(ii, _epicenter.X, jj, _epicenter.Y) <= adjustedMagSquared && !bodyPart.getAtoms()[ii][jj].isLucky()) {
            return false;
        }
        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        _epicenter = new Point2(RNG.next(0, bodyPart.getAtoms().length), RNG.next(0, bodyPart.getAtoms()[0].length));
    }
}