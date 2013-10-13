package sps.particles;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

public class ParticleLease {
    public final ParticleEffectPool.PooledEffect Effect;
    public final String Label;

    public ParticleLease(ParticleEffectPool.PooledEffect effect, String label) {
        Label = label;
        Effect = effect;
    }
}
