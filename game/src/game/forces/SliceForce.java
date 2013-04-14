package game.forces;

import game.creatures.Atom;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.MathHelper;

public class SliceForce extends Force {
    int _magnitude;
    float _dX;
    float _dY;
    Point2 _origin;
    boolean _killRight;
    boolean _killUp;

    public SliceForce() {
        _magnitude = RNG.next(10, 20);
    }

    @Override
    public Atom forceSpecifics(Atom[][] atoms, int ii, int jj) {
        Point2 direction = MathHelper.directionToPointFromLine(new Point2(ii, jj), _origin, _dX, _dY);
        if ((_killRight && direction.X > 0) || (!_killRight && direction.X < 0)) {
            if ((_killUp && direction.Y > 0) || (!_killUp && direction.Y < 0)) {
                return null;
            }
        }
        return atoms[ii][jj];
    }

    @Override
    public void prepareCalculations(Atom[][] atoms) {
        _dX = RNG.coinFlip() ? -1 : 1 * RNG.next(1, _magnitude);
        _dY = RNG.coinFlip() ? -1 : 1 * RNG.next(1, _magnitude);
        _origin = new Point2(RNG.next(_magnitude, atoms.length), RNG.next(_magnitude, atoms[0].length));
        _killRight = RNG.coinFlip();
        _killUp = RNG.coinFlip();
    }
}
