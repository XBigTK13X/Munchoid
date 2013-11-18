package game.tutorial;

import game.GameConfig;
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
        if (GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            StateManager.get().push(new PreloadPopulationOverview());
            return;
        }
        TextPool.get().write("Is this your first time entering a Munchoid tournament?", Screen.pos(5, 50));

        _launchTutorial = new UIButton("Yes", Commands.get("Confirm")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.TutorialEnabled = true;
                options.save();
                StateManager.get().push(new PreloadPopulationOverview());
            }
        };

        _launchGame = new UIButton("No", Commands.get("Push")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.TutorialEnabled = false;
                options.save();
                StateManager.get().push(new PreloadPopulationOverview());
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
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
