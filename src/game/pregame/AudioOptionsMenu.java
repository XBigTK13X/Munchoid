package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.save.Options;
import sps.states.StateManager;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;

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

        final UIButton musicEnabled = new UIButton("Music: " + (Options.load().MusicEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.MusicEnabled = !options.MusicEnabled;
                options.save();
                options.apply();
                setMessage("Music: " + (options.MusicEnabled ? "Enabled" : "Disabled"));
                layout();
            }
        };

        final UIButton soundEnabled = new UIButton("Sound: " + (Options.load().MusicEnabled ? "Enabled" : "Disabled")) {
            @Override
            public void click() {
                Options options = Options.load();
                options.SoundEnabled = !options.SoundEnabled;
                options.save();
                options.apply();
                setMessage("Sound: " + (options.SoundEnabled ? "Enabled" : "Disabled"));
                layout();
            }
        };

        ButtonStyle style = new ButtonStyle(30, 35, 40, 10, 10);
        style.apply(musicEnabled, 0, 2);
        style.apply(soundEnabled, 0, 1);
        style.apply(back, 0, 0);
    }
}
