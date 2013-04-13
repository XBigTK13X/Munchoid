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
        _magnitude = RNG.next(0, 100);

    }

    @Override
    public void apply(BodyPart bodyPart) {
        super.apply(bodyPart);
    }

    @Override
    public Atom forceSpecifics(Atom[][] atoms, int ii, int jj) {

        int adjustedMag = _magnitude + RNG.next(0, 10) - 5;
        int adjustedMagSquared = adjustedMag * adjustedMag;
        if (HitTest.getDistanceSquare(ii, _epicenter.X, jj, _epicenter.Y) <= adjustedMagSquared && !atoms[ii][jj].isLucky()) {
            return null;
        }
        return atoms[ii][jj];
    }

    @Override
    public void prepareCalculations(Atom[][] atoms) {
        _epicenter = new Point2(RNG.next(0, atoms.length), RNG.next(0, atoms[0].length));
    }
}
