package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import sps.states.StateManager;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;

public class OptionsMenu extends OptionsState {
    public OptionsMenu(Sprite background) {
        super(background);
    }

    @Override
    public void create() {
        final UIButton gameplay = new UIButton("Gameplay") {
            @Override
            public void click() {
                StateManager.get().push(new GameOptionsMenu(_background));
            }
        };

        final UIButton video = new UIButton("Video") {
            @Override
            public void click() {
                StateManager.get().push(new VideoOptionsMenu(_background));
            }
        };

        final UIButton audio = new UIButton("Audio") {
            @Override
            public void click() {
                StateManager.get().push(new AudioOptionsMenu(_background));
            }
        };

        final UIButton controls = new UIButton("Controls") {
            @Override
            public void click() {
                StateManager.get().push(new ControlsOptionsMenu(_background));
            }
        };

        final UIButton back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        final UIButton defaults = new UIButton("Reset to Defaults") {
            @Override
            public void click() {
                Options.resetToDefaults();
            }
        };

        ButtonStyle style = new ButtonStyle(12, 30, 25, 10, 10);

        style.apply(gameplay, 0, 3);
        style.apply(video, 1, 3);
        style.apply(audio, 2, 3);

        style.apply(controls, 1, 2);
        style.apply(defaults, 2, 0);
        style.apply(back, 0, 0);
    }
}
