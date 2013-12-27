package game.tutorial;

import game.dev.DevConfig;
import game.population.PreloadPopulationOverview;
import game.pregame.MainMenu;
import game.save.Options;
import game.ui.UIButton;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class TutorialQuery implements State {
    UIButton _launchTutorial;
    UIButton _launchGame;
    UIButton _back;

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
        TextPool.get().write("Is this your first time entering a Munchoid tournament?", Screen.pos(5, 50));

        _launchTutorial = new UIButton("Yes", Commands.get("Confirm")) {
            @Override
            public void click() {
                proceed(true);
            }
        };

        _launchGame = new UIButton("No", Commands.get("Push")) {
            @Override
            public void click() {
                proceed(false);
            }
        };

        _back = new UIButton("Back") {
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
