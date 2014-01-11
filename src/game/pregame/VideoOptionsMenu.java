package game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.core.SettingsDetector;
import game.save.Options;
import sps.display.Screen;
import sps.entities.HitTest;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.ButtonStyle;
import sps.ui.Tooltips;
import sps.ui.UIButton;
import sps.ui.UISlider;

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

        final UIButton settingsDetection = new UIButton("Detect Optimal Settings") {

            @Override
            public void click() {
                StateManager.reset().push(new SettingsDetector());
            }
        };

        final UIButton fullScreen = new UIButton("Toggle Full Screen") {
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

        ButtonStyle style = new ButtonStyle(30, 20, 40, 10, 10);
        style.apply(back, 0, 0);
        style.apply(graphicsMode, 0, 3);
        style.apply(fullScreen, 0, 2);
        style.apply(settingsDetection, 0, 1);

        _brightness = new UISlider(80, 10, (int) Screen.width(10), (int) Screen.height(70)) {
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
        TextPool.get().write("Brightness", Screen.pos(15, 77));

        brightnessReset.setSize(5, 5);
        brightnessReset.setScreenPercent(3, 73);
        Tooltips.get().add(new Tooltips.User() {
            @Override
            public boolean isActive() {
                return HitTest.mouseInside(brightnessReset.getSprite());
            }

            @Override
            public String message() {
                return "Reset brightness.";
            }
        });
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
        return "Graphics Mode: " + (options.GraphicsLowQuality ? "Fast" : "Pretty");
    }

    @Override
    public void draw() {
        super.draw();
        _brightness.draw();
    }
}
