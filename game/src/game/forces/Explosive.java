package game.forces;

import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;

public class Explosive extends BaseForce {
    private int _magnitude;
    private Point2 _epicenter;

    public Explosive() {
        _magnitude = RNG.next(25, 70);

    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {

        int adjustedMag = _magnitude + RNG.next(0, 10) - 5;
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
