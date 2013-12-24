package game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import game.ui.UIButton;
import game.ui.UISlider;
import sps.display.Screen;
import sps.states.StateManager;
import sps.text.TextPool;

public class VideoOptionsMenu extends OptionsState {
    private UISlider _brightness;

    public VideoOptionsMenu(Sprite background) {
        super(background);
    }

    @Override
    public void create() {
        final UIButton back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        final UIButton fullScreen = new UIButton("Full Screen") {
            @Override
            public void click() {
                Options options = Options.load();
                options.FullScreen = !Gdx.graphics.isFullscreen();
                options.save();
                options.apply();
            }
        };

        Options options = Options.load();
        final UIButton graphicsMode = new UIButton(qualityMessage(options)) {
            @Override
            public void click() {
                Options options = Options.load();
                options.GraphicsLowQuality = !options.GraphicsLowQuality;
                options.save();
                options.apply();
                setMessage(qualityMessage(options));
                layout();
            }
        };

        _brightness = new UISlider(80, 10, (int) Screen.width(10), (int) Screen.height(50)) {
            @Override
            public void onSlide() {
                setBrightnessPercent(getSliderPercent(), true);
            }
        };

        setBrightnessPercent(options.Brightness, false);

        final UIButton brightnessReset = new UIButton("") {
            @Override
            public void click() {
                setBrightnessPercent(100, true);
            }
        };

        brightnessReset.setSize(5, 5);
        brightnessReset.setScreenPercent(3, 52);

        graphicsMode.setColRow(1, 1);
        back.setColRow(2, 3);
        fullScreen.setColRow(2, 1);

        graphicsMode.layout();
        back.layout();
        fullScreen.layout();

        TextPool.get().write("Brightness", Screen.pos(15, 55));
    }

    private void setBrightnessPercent(int brightness, boolean persist) {
        _brightness.setSliderPercent(brightness);
        if (persist) {
            Options options = Options.load();
            options.Brightness = brightness;
            options.apply();
            options.save();
        }
    }

    private String qualityMessage(Options options) {
        return "Graphics Mode\n" + (options.GraphicsLowQuality ? "Fast" : "Pretty");
    }

    @Override
    public void draw() {
        super.draw();
        _brightness.draw();
    }
}
