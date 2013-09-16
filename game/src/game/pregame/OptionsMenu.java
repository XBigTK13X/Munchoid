package game.pregame;

import game.save.Options;
import game.ui.UIButton;
import sps.states.State;
import sps.states.StateManager;

public class OptionsMenu implements State {
    private UIButton _gameplay;
    private UIButton _audio;
    private UIButton _video;

    private UIButton _back;
    private UIButton _defaults;

    @Override
    public void create() {
        _gameplay = new UIButton("Gameplay") {
            @Override
            public void click() {
                StateManager.get().push(new GameplayOptionsMenu());
            }
        };

        _video = new UIButton("Video") {
            @Override
            public void click() {
                StateManager.get().push(new VideoOptionsMenu());
            }
        };

        _audio = new UIButton("Audio") {
            @Override
            public void click() {
                StateManager.get().push(new AudioOptionsMenu());
            }
        };

        _back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        _defaults = new UIButton("Reset to Defaults") {
            @Override
            public void click() {
                Options.resetToDefaults();
            }
        };

        _gameplay.setColRow(1, 2);
        _video.setColRow(2, 2);
        _audio.setColRow(3, 2);

        _defaults.setColRow(2, 1);

        _back.setColRow(2, 3);
    }

    @Override
    public void draw() {
        _gameplay.draw();
        _video.draw();
        _audio.draw();
        _defaults.draw();
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
