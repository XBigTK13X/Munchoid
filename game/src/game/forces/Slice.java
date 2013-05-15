package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.MathHelper;

public class Slice extends BaseForce {
    float _dX;
    float _dY;
    Point2 _origin;
    boolean _killRight;
    boolean _killUp;

    public Slice(int magnitude) {
        super(magnitude, GameConfig.SliceScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        Point2 direction = MathHelper.directionToPointFromLine(new Point2(ii, jj), _origin, _dX, _dY);
        if ((_killRight && direction.X > 0) || (!_killRight && direction.X < 0)) {
            if ((_killUp && direction.Y > 0) || (!_killUp && direction.Y < 0)) {
                return false;
            }
        }
        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        _dX = RNG.coinFlip() ? -1 : 1 * RNG.next(1, getScaledMagnitude() + getPartScale(part));
        _dY = RNG.coinFlip() ? -1 : 1 * RNG.next(1, getScaledMagnitude() + getPartScale(part));
        //TODO This doesn't really make a higher magnitude more useful
        // Higher mag = better chance at picking a direction that does more damage
        _origin = new Point2(RNG.next(0, getScaledMagnitude()), RNG.next(0, getScaledMagnitude()));
        _killRight = RNG.coinFlip();
        _killUp = RNG.coinFlip();
    }

    @Override
    public void animate(BodyPart part) {
    }
}
