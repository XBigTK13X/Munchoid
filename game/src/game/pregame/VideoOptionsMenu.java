package game.pregame;

import game.save.Options;
import game.ui.UIButton;
import sps.states.State;
import sps.states.StateManager;

public class VideoOptionsMenu implements State {
    UIButton _back;
    UIButton _fullScreen;

    @Override
    public void create() {
        _back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        _fullScreen = new UIButton("Full Screen") {
            @Override
            public void click() {
                Options options = Options.load();
                options.FullScreen = !options.FullScreen;
                options.save();
                options.apply();
            }
        };

        _back.setColRow(2, 3);

        _fullScreen.setColRow(2, 1);
    }

    @Override
    public void draw() {
        _back.draw();
        _fullScreen.draw();
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
