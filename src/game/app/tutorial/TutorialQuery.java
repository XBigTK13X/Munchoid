package game.app.tutorial;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.dev.DevConfig;
import game.app.save.Options;
import game.stages.population.PreloadPopulationOverview;
import game.stages.pregame.MainMenu;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.display.Screen;
import sps.display.Window;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.UIButton;

public class TutorialQuery implements State {
    private Sprite _background;
    private UIButton _launchTutorial;
    private UIButton _launchGame;
    private UIButton _back;

    public TutorialQuery(Sprite background) {
        _background = background;
    }

    @Override
    public void create() {
        Options options = Options.load();
        if (!options.TutorialQueryEnabled) {
            proceed(false);
            return;
        }

        if (DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            options.TutorialEnabled = false;
            options.apply();
            options.save();

            StateManager.get().push(new PreloadPopulationOverview());
            return;
        }

        TextPool.get().write("Is this your first time entering a Munchoid tournament?", Screen.pos(15, 75));
        Text optionsNotice = TextPool.get().write("*You can disable this question in the Options menu.", Screen.pos(30, 60));
        optionsNotice.setFont("default", 24);

        _launchTutorial = new UIButton("Yes", Commands.get("Menu1")) {
            @Override
            public void click() {
                proceed(true);
            }
        };

        _launchGame = new UIButton("No", Commands.get("Menu2")) {
            @Override
            public void click() {
                proceed(false);
            }
        };

        _back = new UIButton("Back", Commands.get("Menu3")) {
            @Override
            public void click() {
                StateManager.get().rollBackTo(MainMenu.class);
            }
        };

        _launchTutorial.setColRow(1, 1);
        _launchGame.setColRow(2, 1);
        _back.setColRow(3, 1);

        _launchGame.layout();
        _launchTutorial.layout();
        _back.layout();
    }

    private void proceed(boolean showTutorial) {
        Options options = Options.load();
        options.TutorialEnabled = showTutorial;
        options.save();
        StateManager.get().push(new PreloadPopulationOverview());
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
        _launchTutorial.draw();
        _launchGame.draw();
        _back.draw();
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
        return "TutorialQuery";
    }

    @Override
    public void pause() {
    }
}
