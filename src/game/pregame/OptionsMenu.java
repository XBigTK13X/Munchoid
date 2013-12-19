package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import game.ui.UIButton;
import sps.states.StateManager;

public class OptionsMenu extends OptionsState {
    public OptionsMenu(Sprite background) {
        super(background);
    }

    @Override
    public void create() {
        final UIButton gameplay = new UIButton("Gameplay") {
            @Override
            public void click() {
                StateManager.get().push(new GameplayOptionsMenu(_background));
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

        gameplay.setColRow(1, 2);
        video.setColRow(2, 2);
        audio.setColRow(3, 2);

        defaults.setColRow(2, 1);

        back.setColRow(2, 3);
    }
}
