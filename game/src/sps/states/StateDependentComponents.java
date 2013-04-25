package sps.states;

import sps.audio.MusicPlayer;
import sps.entities.EntityManager;
import sps.particles.ParticleEngine;
import sps.text.TextPool;

public class StateDependentComponents {
    public final EntityManager entityManager;
    public final ParticleEngine particleEngine;
    public final TextPool textPool;
    public final MusicPlayer musicPlayer;

    public StateDependentComponents(EntityManager entityManager, ParticleEngine particleEngine, TextPool textPool, MusicPlayer musicPlayer) {
        this.entityManager = entityManager;
        this.particleEngine = particleEngine;
        this.textPool = textPool;
        this.musicPlayer = musicPlayer;
    }
}
