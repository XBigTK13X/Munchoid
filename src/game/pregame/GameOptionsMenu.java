package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import sps.states.StateManager;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;

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

        final UIButton viewIntro = new UIButton("Watch the Intro") {
            @Override
            public void click() {
                StateManager.reset().push(new Intro(true));
            }
        };

        final UIButton introEnabled = new UIButton("Intro Video: " + (Options.load().ShowIntro ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.ShowIntro = !options.ShowIntro;
                options.save();
                options.apply();
                setMessage("Intro Video: " + (options.ShowIntro ? "Enabled" : "Disabled"));
                layout();
            }
        };

        final UIButton tutorialQueryEnabled = new UIButton("Tutorial Prompt: " + (Options.load().TutorialQueryEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.TutorialQueryEnabled = !options.TutorialQueryEnabled;
                options.save();
                options.apply();
                setMessage("Tutorial Prompt: " + (options.TutorialQueryEnabled ? "Enabled" : "Disabled"));
                layout();
            }
        };

        ButtonStyle style = new ButtonStyle(30, 20, 40, 10, 10);
        style.apply(introEnabled, 0, 3);
        style.apply(tutorialQueryEnabled, 0, 2);
        style.apply(viewIntro, 0, 1);
        style.apply(back, 0, 0);
    }
}
