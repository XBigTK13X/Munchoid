package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import game.ui.UIButton;
import sps.states.StateManager;

public class GameOptionsMenu extends OptionsState {
    public GameOptionsMenu(Sprite background) {
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

        final UIButton viewIntro = new UIButton("View Intro") {
            @Override
            public void click() {
                StateManager.reset().push(new Intro(true));
            }
        };

        final UIButton introEnabled = new UIButton("Intro Video\n" + (Options.load().ShowIntro ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.ShowIntro = !options.ShowIntro;
                options.save();
                options.apply();
                setMessage("Intro Video\n" + (options.ShowIntro ? "Enabled" : "Disabled"));
                layout();
            }
        };

        final UIButton tutorialQueryEnabled = new UIButton("Tutorial Prompt\n" + (Options.load().TutorialQueryEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.TutorialQueryEnabled = !options.TutorialQueryEnabled;
                options.save();
                options.apply();
                setMessage("Tutorial Prompt\n" + (options.TutorialQueryEnabled ? "Enabled" : "Disabled"));
                layout();
            }
        };

        viewIntro.setColRow(1, 1);
        introEnabled.setColRow(2, 1);
        tutorialQueryEnabled.setColRow(3, 1);

        back.setColRow(2, 3);

        viewIntro.layout();
        introEnabled.layout();
        tutorialQueryEnabled.layout();
        back.layout();
    }
}
