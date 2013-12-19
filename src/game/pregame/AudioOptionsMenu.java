package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import game.ui.UIButton;
import sps.states.StateManager;

public class AudioOptionsMenu extends OptionsState {
    public AudioOptionsMenu(Sprite background) {
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

        final UIButton musicEnabled = new UIButton("Music\n" + (Options.load().MusicEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.MusicEnabled = !options.MusicEnabled;
                options.save();
                options.apply();
                setMessage("Music\n" + (options.MusicEnabled ? "Enabled" : "Disabled"));
            }
        };

        final UIButton soundEnabled = new UIButton("Sound\n" + (Options.load().MusicEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.SoundEnabled = !options.SoundEnabled;
                options.save();
                options.apply();
                setMessage("Music\n" + (options.SoundEnabled ? "Enabled" : "Disabled"));
            }
        };

        musicEnabled.setColRow(2, 1);

        musicEnabled.setColRow(2, 1);
        back.setColRow(2, 3);
    }
}
