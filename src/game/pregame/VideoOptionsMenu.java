package game.pregame;

import com.badlogic.gdx.Gdx;
import game.save.Options;
import game.ui.UIButton;
import sps.states.State;
import sps.states.StateManager;

public class VideoOptionsMenu implements State {
    UIButton _back;
    UIButton _fullScreen;
    UIButton _graphicsMode;

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
                options.FullScreen = !Gdx.graphics.isFullscreen();
                options.save();
                options.apply();
            }
        };

        _graphicsMode = new UIButton(qualityMessage(Options.load())) {
            @Override
            public void click() {
                Options options = Options.load();
                options.GraphicsLowQuality = !options.GraphicsLowQuality;
                options.save();
                options.apply();
                getMessage().setMessage(qualityMessage(options));
            }
        };

        _graphicsMode.setColRow(1, 1);

        _back.setColRow(2, 3);

        _fullScreen.setColRow(2, 1);
    }

    private String qualityMessage(Options options) {
        return "Graphics Mode\n" + (options.GraphicsLowQuality ? "Fast" : "Pretty");
    }

    @Override
    public void draw() {
        _back.draw();
        _fullScreen.draw();
        _graphicsMode.draw();
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
