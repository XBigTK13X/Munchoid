package sps.particles.behaviors;

import sps.core.RNG;
import sps.particles.Particle2;
import sps.particles.ParticleBehavior;

public class ScrubBehavior extends ParticleBehavior {
    protected static ParticleBehavior __instance;

    public static ParticleBehavior getInstance() {
        if (__instance == null) {
            __instance = new ScrubBehavior();
        }
        return __instance;
    }

    private final float _acceleration = 0.0981f;

    @Override
    public void setup(Particle2 particle) {
        particle.Position.setY(particle.Origin.Y + RNG.next((int) particle.Height * 2, (int) particle.Height * 4));
        particle.Position.setX(particle.Origin.X + (int) (Math.cos(particle.Angle) * RNG.next(-5, 5)));
        downward = RNG.coinFlip();
    }

    //TODO find a way to oscillate. Maybe a behavior registry that calls prep on all behaviors.
    private boolean downward = false;

    @Override
    public void update(Particle2 particle) {
        if (downward) {
            particle.Position.Y += particle.MoveSpeed;
        }
        else {
            particle.Position.Y -= particle.MoveSpeed;
        }
    }
}
