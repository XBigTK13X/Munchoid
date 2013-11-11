package game.tutorial;

import game.GameConfig;
import game.population.PreloadPopulationOverview;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;

public class Tutorial implements State {
    private CoolDown _skip;
    private static final String __defaultSkipInfo = "[Space] to skip";
    private Text _skipInfo;

    @Override
    public void create() {
        _skip = new CoolDown(GameConfig.IntroVideoSkipSeconds);
        _skipInfo = TextPool.get().write(__defaultSkipInfo, Screen.pos(80, 10));

    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (_skip.isCooled() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            StateManager.get().push(new PreloadPopulationOverview());
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
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
