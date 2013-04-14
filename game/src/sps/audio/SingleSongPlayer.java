package sps.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import sps.core.Loader;

public class SingleSongPlayer extends MusicPlayer {
    private final Music song;

    public SingleSongPlayer(String fileName) {
        FileHandle mainThemeFh = new FileHandle(Loader.get().music(fileName));
        song = Gdx.audio.newMusic(mainThemeFh);
        song.setLooping(true);
    }

    @Override
    public void start() {
        song.play();
    }


    @Override
    public void stop() {
        song.stop();
    }
}
