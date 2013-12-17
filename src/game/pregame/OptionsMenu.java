package game.pregame;

import game.save.Options;
import game.ui.UIButton;
import sps.states.StateManager;

public class OptionsMenu extends OptionsState {
    @Override
    public void create() {
        final UIButton gameplay = new UIButton("Gameplay") {
            @Override
            public void click() {
                StateManager.get().push(new GameplayOptionsMenu());
            }
        };

        final UIButton video = new UIButton("Video") {
            @Override
            public void click() {
                StateManager.get().push(new VideoOptionsMenu());
            }
        };

        final UIButton audio = new UIButton("Audio") {
            @Override
            public void click() {
                StateManager.get().push(new AudioOptionsMenu());
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
