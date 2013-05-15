package sps.particles.behaviors;

import sps.core.RNG;
import sps.entities.HitTest;
import sps.particles.Particle2;
import sps.particles.ParticleBehavior;

public class ContractBehavior extends ParticleBehavior {
    protected static ParticleBehavior __instance;

    public static ParticleBehavior getInstance() {
        if (__instance == null) {
            __instance = new ContractBehavior();
        }
        return __instance;
    }

    @Override
    public void update(Particle2 particle) {
        particle.Radius = HitTest.getDistance(particle.Position.X, particle.Position.Y, particle.Origin.X, particle.Origin.Y);
        if (particle.Radius <= 5) {
            particle.Angle = RNG.angle();
            particle.MoveSpeed += RNG.next(2, 4);
            particle.Position.setX(particle.Position.X + (float) Math.cos(particle.Angle) * particle.MoveSpeed * 10);
            particle.Position.setY(particle.Position.Y + (float) Math.sin(particle.Angle) * particle.MoveSpeed * 10);
        }
        else {
            particle.Position.setX(particle.Position.X + (float) Math.cos(particle.Angle) * -particle.MoveSpeed);
            particle.Position.setY(particle.Position.Y + (float) Math.sin(particle.Angle) * -particle.MoveSpeed);
        }
    }
}
