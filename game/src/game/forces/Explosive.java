package game.forces;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;
import sps.particles.ParticleEngine;
import sps.particles.behaviors.ExplodeBehavior;

public class Explosive extends BaseForce {
    private Point2 _epicenter;

    public Explosive(int magnitude) {
        super(magnitude, GameConfig.ExplosiveScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        int adjustedMag = (getMagnitude() + RNG.next(-getMagnitude(), getMagnitude())) * (getScale() + getPartScale(part));
        if (HitTest.getDistance(ii, jj, _epicenter.X, _epicenter.Y) <= adjustedMag && !part.getAtoms()[ii][jj].isLucky()) {
            return false;
        }
        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        _epicenter = new Point2(RNG.next(0, part.getAtoms().length), RNG.next(0, part.getAtoms()[0].length));
    }

    @Override
    public void animate(BodyPart part) {
        ParticleEngine.get().emit(ExplodeBehavior.getInstance(), part.getGlobalPosition(), Color.WHITE);
    }
}