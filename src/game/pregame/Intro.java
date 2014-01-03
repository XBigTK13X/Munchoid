package game.pregame;

import game.core.GameConfig;
import game.core.InputWrapper;
import game.dev.DevConfig;
import sps.audio.MusicPlayer;
import sps.bridge.Commands;
import sps.color.Color;
import sps.display.Screen;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;

public class Intro implements State {
    private CoolDown _skip;
    private Text _skipInfo;
    private static final String __defaultSkipInfo = "[SPACE to skip]";

    private String _introText = "";
    private String _readText = "";

    private boolean _enabled;
    private int _index;
    private Text _introDisplay;
    private CoolDown _typingSpeed = new CoolDown(.05f);

    public Intro(boolean enabled) {
        _enabled = enabled;
    }

    @Override
    public void create() {
        if (!_enabled) {
            StateManager.get().push(new PreloadMainMenu());
        }
        else {
            _introText += "We created the Munchoid. A collection of untapped mental fragments.\n\n";
            _introText += "Citizens volunteer their unused thoughts and we convert them into digital warriors.\n";
            _introText += "The strongest can be used to prevent death.\n\n";
            _introText += "There is always a problem that needs solving, and people hoping to gain a little fame.\n";
            _introText += "Men and women flock to Munchoid Arena.\nA modern day colosseum where Munchoids battle it out for the sake of humankind.\n\n";
            _introText += "An epidemic is spreading over the planet.\n";
            _introText += "Can you grow Munchoids strong enough to save your home region?\n";
            _introText += "Step forward, lend us your thoughts, and may the people be correct in selecting you to fight for them.\n\n";
            _introText += "(This intro can be disabled in the Options menu. Press " + Commands.get("Confirm") + " to begin.)";

            _skip = new CoolDown(GameConfig.IntroVideoSkipSeconds);
            _skipInfo = TextPool.get().write(__defaultSkipInfo, Screen.pos(80, 10));
            Color bgText = new Color(.5f, .5f, .5f, .75f);
            _skipInfo.setColor(bgText);

            _introDisplay = TextPool.get().write("", Screen.pos(5, 95));
            _introDisplay.setFont("Console", 30);
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (!_readText.equals(_introText)) {
            if (_typingSpeed.updateAndCheck()) {
                _readText = _introText.substring(0, _index++);
                _introDisplay.setMessage(_readText);
            }
        }
        else {
            MusicPlayer.get().stop();
        }

        if ((_readText.equals(_introText) && InputWrapper.confirm()) || _skip.isCooled() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            StateManager.get().push(new PreloadMainMenu());
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
        MusicPlayer.get().play("Intro");
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
