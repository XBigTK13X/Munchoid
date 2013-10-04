package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;
import sps.particles.ParticleWrapper;

public class Explosive extends BaseForce {
    private Point2 _epicenter;
    private int _adjustedMagnitude;
    private static final int __jitter = 10;

    public Explosive(int magnitude) {
        super(magnitude, GameConfig.ExplosiveScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        int jitteredMagnitude = _adjustedMagnitude + RNG.next(__jitter) - __jitter / 2;
        if (HitTest.getDistance(ii, jj, _epicenter.X, _epicenter.Y) <= jitteredMagnitude) {
            return false;
        }
        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        _epicenter = new Point2(RNG.next(0, part.getAtoms().length), RNG.next(0, part.getAtoms()[0].length));
        _adjustedMagnitude = getScaledMagnitude() + RNG.next(getPartScale(part));
    }

    @Override
    public void animate(BodyPart part) {
        ParticleWrapper.get().emit("Explosion", part.getExpensiveGlobalPosition());
    }
}