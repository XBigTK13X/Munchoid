package sps.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import sps.core.Loader;
import sps.core.SpsConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicPlayer {
    private static MusicPlayer __instance;

    public static MusicPlayer get() {
        if (__instance == null) {
            __instance = new MusicPlayer();
        }
        return __instance;
    }

    private ExecutorService musicExecutor = Executors.newSingleThreadExecutor();
    private Map<String, Music> _music = new HashMap<>();

    private MusicPlayer() {

    }

    public void add(String id, String nameWithExtension) {
        _music.put(id, Gdx.audio.newMusic(new FileHandle(Loader.get().music(nameWithExtension))));
    }

    public void play(final String musicId) {
        stop();
        play(musicId, true);
    }

    public void play(final String musicId, final boolean loop) {
        if (SpsConfig.get().musicEnabled) {
            musicExecutor.submit(new Runnable() {
                public void run() {
                    _music.get(musicId).setLooping(loop);
                    _music.get(musicId).play();
                }
            });
        }
    }

    public void stop() {
        for (String id : _music.keySet()) {
            _music.get(id).stop();
        }
    }

    public Music music(String id) {
        return _music.get(id);
    }

    public void dispose() {
        for (String id : _music.keySet()) {
            _music.get(id).dispose();
        }
    }
}
