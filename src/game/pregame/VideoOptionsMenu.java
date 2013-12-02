package game.pregame;

import com.badlogic.gdx.Gdx;
import game.save.Options;
import game.ui.UIButton;
import game.ui.UISlider;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;

public class VideoOptionsMenu implements State {
    UIButton _back;
    UIButton _fullScreen;
    UIButton _graphicsMode;
    UISlider _brightness;
    UIButton _brightnessReset;

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

        Options options = Options.load();
        _graphicsMode = new UIButton(qualityMessage(options)) {
            @Override
            public void click() {
                Options options = Options.load();
                options.GraphicsLowQuality = !options.GraphicsLowQuality;
                options.save();
                options.apply();
                setMessage(qualityMessage(options));
            }
        };

        _brightness = new UISlider(80, 10, (int) Screen.width(10), (int) Screen.height(50)) {
            @Override
            public void onSlide() {
                int brightness = getSliderPercent() - 50;
                setBrightness(brightness);
            }
        };

        _brightness.setSliderPercent(options.Brightness + 50);

        _brightnessReset = new UIButton("") {
            @Override
            public void click() {
                setBrightness(0);
            }
        };


        _brightnessReset.setSize(5, 5);
        _brightnessReset.setScreenPercent(1, 50);

        _graphicsMode.setColRow(1, 1);
        _back.setColRow(2, 3);
        _fullScreen.setColRow(2, 1);
    }

    private void setBrightness(int brightness) {
        _brightness.setSliderPercent(brightness + 50);
        Options options = Options.load();
        options.Brightness = brightness;
        options.apply();
        options.save();
    }

    private String qualityMessage(Options options) {
        return "Graphics Mode\n" + (options.GraphicsLowQuality ? "Fast" : "Pretty");
    }

    @Override
    public void draw() {
        _back.draw();
        _fullScreen.draw();
        _graphicsMode.draw();
        _brightness.draw();
        _brightnessReset.draw();
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
