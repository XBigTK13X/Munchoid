package game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.core.BackgroundMaker;
import game.core.InputWrapper;
import game.core.Score;
import game.dev.DevConfig;
import game.save.Persistence;
import game.save.RestoreSavedGame;
import game.tutorial.TutorialQuery;
import org.apache.commons.io.FileUtils;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.core.Loader;
import sps.core.Logger;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.UIButton;

import java.io.File;

public class MainMenu implements State {
    private Sprite _background;
    private Sprite _logo;

    @Override
    public void create() {
        _background = BackgroundMaker.noisyRadialDark();
        StateManager.clearTimes();
        StateManager.get().clearTutorialCompletions();
        Score.reset();

        _logo = SpriteMaker.fromGraphic("munchoid_logo.png");

        _logo.setPosition(Screen.centerWidth((int) _logo.getWidth()), Screen.height(80));

        UIButton _start = new UIButton("Start", Commands.get("Confirm")) {
            @Override
            public void click() {
                start();
            }
        };

        UIButton _options = new UIButton("Options", Commands.get("Push")) {
            @Override
            public void click() {
                StateManager.get().push(new OptionsMenu(_background));
            }
        };

        UIButton _exit = new UIButton("Exit") {
            @Override
            public void click() {
                Gdx.app.exit();
            }
        };

        _start.setColRow(1, 1);
        _options.setColRow(2, 1);
        _exit.setColRow(3, 1);

        _start.layout();
        _options.layout();
        _exit.layout();

        if (Persistence.get().saveFileExists()) {
            final UIButton _load = new UIButton("Continue") {
                @Override
                public void click() {
                    try {
                        StateManager.get().push(new RestoreSavedGame());
                    }
                    catch (RuntimeException e) {
                        setVisible(false);
                        if (e.getMessage() != null) {
                            TextPool.get().write("Unable to load the save file.\n" + e.getMessage(), Screen.pos(40, 70));
                        }
                        else {
                            Logger.exception(e);
                        }
                    }
                }
            };
            _load.setColRow(2, 2);
            _load.layout();
        }

        String version = "Unknown";
        File versionDat = Loader.get().data("version.dat");
        if (versionDat.exists()) {
            try {
                version = FileUtils.readFileToString(versionDat);
            }
            catch (Exception e) {
                Logger.exception(e, false);
            }
        }
        Text versionDisplay = TextPool.get().write("Version " + version, Screen.pos(5, 5));
        versionDisplay.setFont("Console", 24);

        Text developedDisplay = TextPool.get().write("Developed by Simple Path Studios", Screen.pos(40, 5));
        developedDisplay.setFont("Console", 24);

        Text twitterDisplay = TextPool.get().write("Twitter @XBigTK13X", Screen.pos(80, 5));
        twitterDisplay.setFont("Console", 24);
    }

    private void start() {
        StateManager.get().push(new TutorialQuery());
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
        Window.get().schedule(_logo, DrawDepths.get("Logo"));
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            start();
        }
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
        return "Main Menu";
    }

    @Override
    public void pause() {
    }
}
