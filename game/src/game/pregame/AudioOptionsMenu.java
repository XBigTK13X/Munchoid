package game.pregame;

import game.save.Options;
import game.ui.UIButton;
import sps.states.State;
import sps.states.StateManager;

public class AudioOptionsMenu implements State {
    UIButton _back;
    UIButton _musicEnabled;

    @Override
    public void create() {
        _back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        _musicEnabled = new UIButton("Music\n" + (Options.load().MusicEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.MusicEnabled = !options.MusicEnabled;
                options.save();
                options.apply();
                getMessage().setMessage("Music\n" + (options.MusicEnabled ? "Enabled" : "Disabled"));
            }
        };

        _musicEnabled.setColRow(2, 1);
        _back.setColRow(2, 3);
    }

    @Override
    public void draw() {
        _musicEnabled.draw();
        _back.draw();
    }

    @Override
    public void update() {
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
