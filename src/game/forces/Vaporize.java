package game.forces;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;
import sps.particles.ParticleWrapper;

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
        ParticleEffect effect = ParticleWrapper.get().emit("vaporize", part.getCheapGlobalCenter());
        int m = Math.max(part.getWidth(), part.getHeight()) / 2;
        ParticleWrapper.setSquareBounds(effect, m);
    }
}
