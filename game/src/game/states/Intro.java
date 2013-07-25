package game.states;

import game.GameConfig;
import game.movie.Movie;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;

public class Intro implements State {
    private Movie _movie;

    @Override
    public void create() {
        _movie = new Movie();
        _movie.addStrip(.8f, "We created the Munchoid. A collection of untapped mental fragments.");
        _movie.addStrip(5.9f, "Citizens volunteer their unused thoughts and we convert them into digital warriors.");
        _movie.addStrip(12.2f, "The strongest could be used to unlock the secrets of our world.");
        _movie.addStrip(16.5f, "There’s always a problem that needs solving, and honest people hoping to gain a little fame.");
        _movie.addStrip(22.9f, "Men and women flock to Munchoid Arena. A modern day coliseum where munchoids battle it out for the sake of humankind.");
        _movie.addStrip(32f, "How can you make this world a better place?");
        _movie.addStrip(35f, "Step forward, lend us your thoughts, and may we all learn from one another.");
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        _movie.play(MusicPlayer.get().getMusic().getPosition());

        if (Input.get().isActive(Commands.get("Confirm")) || !MusicPlayer.get().getMusic().isPlaying() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            StateManager.get().push(new MainMenu());
        }

    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        MusicPlayer.get(new SingleSongPlayer("Intro.ogg", false));
        MusicPlayer.get().start();
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return "Intro";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
