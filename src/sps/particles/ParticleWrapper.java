package sps.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import sps.bridge.DrawDepths;
import sps.core.Loader;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.display.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleWrapper {
    private static final int __initialPoolCapacity = 5;

    private static ParticleWrapper __instance;

    public static ParticleWrapper get() {
        if (__instance == null) {
            __instance = new ParticleWrapper();
        }
        return __instance;
    }

    private Map<String, ParticleEffectPool> _effectPools;

    private List<ParticleLease> _leasedEffects;

    private ParticleWrapper() {
        _leasedEffects = new ArrayList<>();

        _effectPools = new HashMap<>();
        for (File f : Loader.get().graphics("particles").listFiles()) {
            String effectLabel = f.getName().replace(".pe", "");
            ParticleEffect pe = new ParticleEffect();
            pe.load(Gdx.files.internal(f.getAbsolutePath()), Gdx.files.internal(Loader.get().graphics("").getAbsolutePath()));
            _effectPools.put(effectLabel, new ParticleEffectPool(pe, __initialPoolCapacity, SpsConfig.get().particleEffectPoolLimit));
        }
    }

    public void release() {
        for (int ii = 0; ii < _leasedEffects.size(); ii++) {
            remove(ii);
        }
    }

    private void remove(int ii) {
        if (ii < _leasedEffects.size()) {
            _effectPools.get(_leasedEffects.get(ii).Label).free(_leasedEffects.get(ii).Effect);
            _leasedEffects.remove(ii);
        }
    }

    public ParticleEffect emit(String effectLabel, Point2 position) {
        String label = effectLabel.toLowerCase();
        ParticleEffectPool.PooledEffect effect = _effectPools.get(label).obtain();
        effect.setPosition(position.X, position.Y);
        effect.start();
        _leasedEffects.add(new ParticleLease(effect, label));
        return effect;
    }

    public void update() {
        for (int ii = 0; ii < _leasedEffects.size(); ii++) {
            if (_leasedEffects.get(ii).Effect.isComplete()) {
                remove(ii);
                ii--;
            }
        }
    }

    public void draw() {
        for (ParticleLease lease : _leasedEffects) {
            Window.get().schedule(lease, DrawDepths.get("Particle"));
        }
    }
}
