package game.forces;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;
import sps.particles.ParticleEngine;
import sps.particles.behaviors.VaporizeBehavior;

public class Vaporize extends BaseForce {
    public Vaporize(int magnitude) {
        super(magnitude, GameConfig.VaporizeScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        return RNG.percent(getScaledMagnitude() + getPartScale(part)) ? false : part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void animate(BodyPart part) {
        ParticleEngine.get().emit(VaporizeBehavior.getInstance(), part.getGlobalPosition(), Color.WHITE);
    }
}
