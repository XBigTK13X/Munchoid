package game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.BackgroundMaker;
import game.DevConfig;
import game.InputWrapper;
import game.Score;
import game.save.Persistence;
import game.tutorial.TutorialQuery;
import game.ui.UIButton;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.core.Logger;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class MainMenu implements State {
    Sprite _background;

    UIButton _start;
    UIButton _options;
    UIButton _exit;
    UIButton _load;

    private Sprite _logo;

    @Override
    public void create() {
        _background = BackgroundMaker.noisyRadialDark();
        StateManager.clearTimes();
        StateManager.get().clearTutorialCompletions();
        Score.reset();

        _logo = SpriteMaker.fromGraphic("munchoid_logo.png");

        _logo.setPosition(Screen.centerWidth((int) _logo.getWidth()), Screen.height(80));

        _start = new UIButton("Start", Commands.get("Confirm")) {
            @Override
            public void click() {
                StateManager.get().push(new TutorialQuery());
            }
        };

        _options = new UIButton("Options", Commands.get("Push")) {
            @Override
            public void click() {
                StateManager.get().push(new OptionsMenu(_background));
            }
        };

        _exit = new UIButton("Exit") {
            @Override
            public void click() {
                Gdx.app.exit();
            }
        };

        _start.setColRow(1, 1);
        _options.setColRow(2, 1);
        _exit.setColRow(3, 1);

        if (Persistence.get().saveFileExists()) {
            _load = new UIButton("Continue") {
                @Override
                public void click() {
                    try {
                        StateManager.get().loadFrom(Persistence.get().autoLoad());
                    }
                    catch (RuntimeException e) {
                        _load.setVisible(false);
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
        }
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
        Window.get().schedule(_logo, DrawDepths.get("Logo"));
        _start.draw();
        _options.draw();
        _exit.draw();
        if (_load != null) {
            _load.draw();
        }
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            _start.click();
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        Persistence.get().configLoad();
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return "Main Menu";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
