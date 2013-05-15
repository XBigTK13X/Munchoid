package sps.particles.behaviors;

import sps.core.RNG;
import sps.particles.Particle2;
import sps.particles.ParticleBehavior;

public class VaporizeBehavior extends ParticleBehavior {
    protected static ParticleBehavior __instance;

    public static ParticleBehavior getInstance() {
        if (__instance == null) {
            __instance = new VaporizeBehavior();
        }
        return __instance;
    }

    @Override
    public void setup(Particle2 particle) {
        particle.Position.setX(particle.Origin.X + RNG.next((int) particle.Width * 2, (int) particle.Width * 4));
        particle.Position.setY(particle.Origin.Y + RNG.next((int) particle.Height * 2, (int) particle.Height * 4));
    }

    private final float _accel = .95f;

    @Override
    public void update(Particle2 particle) {
        particle.Height = particle.Width *= _accel;
    }
}
