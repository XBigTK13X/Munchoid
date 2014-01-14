package game.stages.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.save.Options;
import sps.bridge.Commands;
import sps.states.StateManager;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;

public class GameOptionsMenu extends OptionsState {
    public GameOptionsMenu(Sprite background) {
        super(background);
    }

    @Override
    public void create() {
        Options options = Options.load();
        final UIButton back = new UIButton("Back", Commands.get("Menu6")) {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        final UIButton commandLabels = new UIButton("Keyboard Labels: " + (options.GUIButtonKeyboardLabels ? "Enabled" : "Disabled"), Commands.get("Menu3")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.GUIButtonKeyboardLabels = !options.GUIButtonKeyboardLabels;
                options.save();
                options.apply();
                setMessage("Keyboard Labels: " + (options.GUIButtonKeyboardLabels ? "Enabled" : "Disabled"));
                layout();
            }
        };

        final UIButton viewIntro = new UIButton("Watch the Intro", Commands.get("Menu4")) {
            @Override
            public void click() {
                StateManager.reset().push(new Intro(true));
            }
        };

        final UIButton introEnabled = new UIButton("Intro Video: " + (options.ShowIntro ? "Enabled" : "Disabled"), Commands.get("Menu1")) {
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

        final UIButton tutorialQueryEnabled = new UIButton("Tutorial Prompt: " + (options.TutorialQueryEnabled ? "Enabled" : "Disabled"), Commands.get("Menu2")) {
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

        ButtonStyle style = new ButtonStyle(10, 20, 80, 10, 10);
        style.apply(introEnabled, 0, 4);
        style.apply(commandLabels, 0, 3);
        style.apply(tutorialQueryEnabled, 0, 2);
        style.apply(viewIntro, 0, 1);
        style.apply(back, 0, 0);
    }
}
