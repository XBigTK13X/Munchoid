package sps.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import sps.core.Loader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundPlayer {
    private static SoundPlayer __instance;

    public static SoundPlayer get() {
        if (__instance == null) {
            __instance = new SoundPlayer();
        }
        return __instance;
    }

    private SoundPlayer() {

    }

    ExecutorService soundExecutor = Executors.newSingleThreadExecutor();

    Map<String, Sound> _sounds = new HashMap<>();

    public void add(String id, String nameWithExtension) {
        _sounds.put(id, Gdx.audio.newSound(new FileHandle(Loader.get().sound(nameWithExtension))));
    }

    public void play(final String soundId) {
        soundExecutor.submit(new Runnable() {
            public void run() {
                _sounds.get(soundId).play();
            }
        });
    }

}
