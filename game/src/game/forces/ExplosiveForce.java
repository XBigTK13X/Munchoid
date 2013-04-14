package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;

public class ExplosiveForce extends Force {
    private int _magnitude;
    private Point2 _epicenter;

    public ExplosiveForce() {
        _magnitude = RNG.next(25, 70);

    }

    @Override
    public Atom forceSpecifics(BodyPart bodyPart, int ii, int jj) {

        int adjustedMag = _magnitude + RNG.next(0, 10) - 5;
        int adjustedMagSquared = adjustedMag * adjustedMag;
        if (HitTest.getDistanceSquare(ii, _epicenter.X, jj, _epicenter.Y) <= adjustedMagSquared && !bodyPart.getAtoms()[ii][jj].isLucky()) {
            return null;
        }
        return bodyPart.getAtoms()[ii][jj];
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        _epicenter = new Point2(RNG.next(0, bodyPart.getAtoms().length), RNG.next(0, bodyPart.getAtoms()[0].length));
    }
}
