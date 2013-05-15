package sps.particles.behaviors;

import sps.particles.Particle2;
import sps.particles.ParticleBehavior;

public class ExplodeBehavior extends ParticleBehavior {
    protected static ParticleBehavior __instance;

    public static ParticleBehavior getInstance() {
        if (__instance == null) {
            __instance = new ExplodeBehavior();
        }
        return __instance;
    }

    private final float _acceleration = 0.0981f;

    @Override
    public void setup(Particle2 particle) {
        particle.Position.setX(particle.Position.X + (float) Math.cos(particle.Angle) * (particle.Width + particle.Height));
        particle.Position.setY(particle.Position.Y + (float) Math.sin(particle.Angle) * (particle.Height + particle.Height));
    }

    @Override
    public void update(Particle2 particle) {
        particle.MoveSpeed += _acceleration * Math.abs(particle.Angle / 10);
        particle.Position.setY(particle.Position.Y - particle.MoveSpeed);
    }
}
