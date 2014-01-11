package game.stages.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.ArenaScore;
import game.app.core.InputWrapper;
import game.app.dev.DevConfig;
import game.app.save.Persistence;
import game.app.save.RestoreSavedGame;
import game.app.tutorial.TutorialQuery;
import game.app.tutorial.Tutorials;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.display.Screen;
import sps.display.Window;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;

public class MainMenu implements State {
    private Sprite _background;
    private Sprite _logo;
    private Text _corruptSave;
    private UIButton _load;
    private String _version;
    private boolean _saveFilePresent;

    public MainMenu(MainMenuPayload payload) {
        _background = payload.Background;
        _logo = payload.Logo;
        _version = payload.Version;
        _saveFilePresent = payload.SaveFilePresent;
    }


    @Override
    public void create() {
        _logo.setPosition(Screen.centerWidth((int) _logo.getWidth()), Screen.height(80));

        UIButton _start = new UIButton("New Game", Commands.get("Confirm")) {
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

        ButtonStyle style = new ButtonStyle(30, 20, 40, 10, 10);
        style.apply(_start, 0, 2);
        style.apply(_options, 0, 1);
        style.apply(_exit, 0, 0);
        if (_saveFilePresent) {
            _load = new UIButton("Continue") {
                @Override
                public void click() {
                    StateManager.get().push(new RestoreSavedGame());
                }
            };
            style.apply(_load, 0, 3);
            _corruptSave = TextPool.get().write("\t\t\t\t\tUnable to load the save file.\n It is most likely from an older version of the game.", Screen.pos(20, 70));
            _corruptSave.setVisible(false);
        }


        Text versionDisplay = TextPool.get().write("Version " + _version, Screen.pos(5, 5));
        versionDisplay.setFont("Console", 24);

        Text developedDisplay = TextPool.get().write("Developed by Simple Path Studios", Screen.pos(40, 5));
        developedDisplay.setFont("Console", 24);

        Text twitterDisplay = TextPool.get().write("Twitter @XBigTK13X", Screen.pos(80, 5));
        twitterDisplay.setFont("Console", 24);
    }

    private void start() {
        StateManager.get().push(new TutorialQuery(_background));
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
        Window.get().schedule(_logo, DrawDepths.get("Logo"));
    }

    @Override
    public void update() {
        if (Persistence.get().isSaveBad() && !_corruptSave.isVisible()) {
            _corruptSave.setVisible(true);
            _load.setVisible(false);
        }
        if (InputWrapper.confirm() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            start();
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        StateManager.clearTimes();
        Tutorials.get().clearCompletion();
        ArenaScore.reset();
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
