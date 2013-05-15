package game.forces;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;
import sps.particles.ParticleEngine;
import sps.particles.behaviors.RadiateBehavior;

public class Expansion extends BaseForce {
    public Expansion(int magnitude) {
        super(magnitude, GameConfig.ExpansionScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        part.setScale(part.getScale() + RNG.next(getScaledMagnitude(), (int) ((getScaledMagnitude() + getPartScale(part)) * 1.1f)) / 100f);
    }

    @Override
    public void animate(BodyPart part) {
        ParticleEngine.get().emit(RadiateBehavior.getInstance(), part.getGlobalPosition(), Color.WHITE);
    }
}
