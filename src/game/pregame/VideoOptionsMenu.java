package game.pregame;

import com.badlogic.gdx.Gdx;
import game.save.Options;
import game.ui.UIButton;
import game.ui.UISlider;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

public class VideoOptionsMenu implements State {
    private UIButton _back;
    private UIButton _fullScreen;
    private UIButton _graphicsMode;
    private UISlider _brightness;
    private UIButton _brightnessReset;
    private Text _brightnessLabel;

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
                setBrightnessPercent(getSliderPercent());
            }
        };

        setBrightnessPercent(options.Brightness);

        _brightnessReset = new UIButton("") {
            @Override
            public void click() {
                setBrightnessPercent(100);
            }
        };

        _brightnessReset.setSize(5, 5);
        _brightnessReset.setScreenPercent(3, 52);
        _graphicsMode.setColRow(1, 1);
        _back.setColRow(2, 3);
        _fullScreen.setColRow(2, 1);

        _brightnessLabel = TextPool.get().write("Brightness", Screen.pos(15, 55));
    }

    private void setBrightnessPercent(int brightness) {
        _brightness.setSliderPercent(brightness);
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
