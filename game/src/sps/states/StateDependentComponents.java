package sps.states;

import sps.entities.EntityManager;
import sps.particles.ParticleEngine;
import sps.text.TextPool;

public class StateDependentComponents {
    public final EntityManager entityManager;
    public final ParticleEngine particleEngine;
    public final TextPool textPool;

    public StateDependentComponents(EntityManager entityManager, ParticleEngine particleEngine, TextPool textPool) {
        this.entityManager = entityManager;
        this.particleEngine = particleEngine;
        this.textPool = textPool;
    }
}
