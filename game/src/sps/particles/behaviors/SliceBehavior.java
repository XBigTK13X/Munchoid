package sps.particles.behaviors;

import sps.core.RNG;
import sps.particles.Particle2;
import sps.particles.ParticleBehavior;

public class SliceBehavior extends ParticleBehavior {
    protected static ParticleBehavior __instance;

    public static ParticleBehavior getInstance() {
        if (__instance == null) {
            __instance = new SliceBehavior();
        }
        return __instance;
    }

    @Override
    public int getParticleCount() {
        return 10;
    }

    @Override
    public void setup(Particle2 particle) {
        float slope = RNG.next(-10, 10) / 10f;
        float x = RNG.next((int) particle.Width, (int) particle.Width * 4);
        float y = slope * x;
        particle.Position.setX(particle.Origin.X + x);
        particle.Position.setY(particle.Origin.Y + y);
    }

    private final float _accel = .95f;


    @Override
    public void update(Particle2 particle) {
        particle.Height = particle.Width *= _accel;
    }
}
