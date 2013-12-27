package game.pregame;

import game.GameConfig;
import game.dev.DevConfig;
import sps.audio.MusicPlayer;
import sps.bridge.Commands;
import sps.color.Color;
import sps.display.Screen;
import sps.io.Input;
import sps.movie.Movie;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;

public class Intro implements State {
    private Movie _movie;
    private CoolDown _skip;
    private Text _skipInfo;
    private static final String __defaultSkipInfo = "[SPACE to skip]";

    private boolean _enabled;

    public Intro(boolean enabled) {
        _enabled = enabled;
    }

    @Override
    public void create() {
        if (!_enabled) {
            StateManager.get().push(new MainMenu());
        }
        else {
            _movie = new Movie();
            _movie.addStrip(.8f, "We created the Munchoid. A collection of untapped mental fragments.");
            _movie.addStrip(5.9f, "Citizens volunteer their unused thoughts and we convert them into digital warriors.");
            _movie.addStrip(12.2f, "The strongest could be used to unlock the secrets of our world.");
            _movie.addStrip(16.5f, "There’s always a problem that needs solving, and honest people hoping to gain a little fame.");
            _movie.addStrip(22.9f, "Men and women flock to Munchoid Arena. A modern day coliseum where munchoids battle it out for the sake of humankind.");
            _movie.addStrip(32f, "How can you make this world a better place?");
            _movie.addStrip(35f, "Step forward, lend us your thoughts, and may we all learn from one another.");

            _skip = new CoolDown(GameConfig.IntroVideoSkipSeconds);
            _skipInfo = TextPool.get().write(__defaultSkipInfo, Screen.pos(80, 10));
            Color bgText = new Color(.5f, .5f, .5f, .75f);
            _skipInfo.setColor(bgText);
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        _movie.play(MusicPlayer.get().music("Intro").getPosition());

        if (_skip.isCooled() || !MusicPlayer.get().music("Intro").isPlaying() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            StateManager.get().push(new MainMenu());
        }

        if (Input.get().isActive(Commands.get("Confirm"), 0, false)) {
            _skip.update();
            _skipInfo.setMessage("Skip in " + String.format("%.2f", _skip.getSecondsLeft()) + " sec");
        }
        else {
            _skip.reset();
            _skipInfo.setMessage(__defaultSkipInfo);
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        MusicPlayer.get().play("Intro", false);
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return "Intro";
    }

    @Override
    public void pause() {
    }
}
