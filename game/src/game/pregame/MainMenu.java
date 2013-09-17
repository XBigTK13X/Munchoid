package game.pregame;

import com.badlogic.gdx.Gdx;
import game.GameConfig;
import game.InputWrapper;
import game.Score;
import game.population.PreloadPopulationOverview;
import game.save.Persistence;
import game.ui.UIButton;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class MainMenu implements State {
    UIButton _start;
    UIButton _options;
    UIButton _exit;
    UIButton _load;

    @Override
    public void create() {
        TextPool.get().write("Munchoid", Screen.pos(20, 90));
        StateManager.clearTimes();
        Score.reset();

        _start = new UIButton("Start", Commands.get("Confirm")) {
            @Override
            public void click() {
                StateManager.get().push(new PreloadPopulationOverview());
            }
        };

        _options = new UIButton("Options", Commands.get("Push")) {
            @Override
            public void click() {
                StateManager.get().push(new OptionsMenu());
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
            _load.setColRow(2, 3);
        }
    }

    @Override
    public void draw() {
        _start.draw();
        _options.draw();
        _exit.draw();
        if (_load != null) {
            _load.draw();
        }
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
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
