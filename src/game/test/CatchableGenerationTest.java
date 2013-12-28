package game.test;

import game.arena.Catchable;
import game.core.GameConfig;
import game.core.InputWrapper;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.io.Input;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

import java.util.ArrayList;
import java.util.List;

public class CatchableGenerationTest implements State {

    private long _testTime;
    private long _startTime;

    private int _testStage = 0;

    Text _display;

    private List<Catchable> _catchables;

    @Override
    public void create() {
        _display = TextPool.get().write("Press " + Commands.get("Confirm") + " to run the test", Screen.pos(5, 50));
        _catchables = new ArrayList<>();
    }

    @Override
    public void draw() {
        for (Catchable c : _catchables) {
            c.draw();
        }
    }

    @Override
    public void update() {
        int creatureCount = 10;
        if (Input.get().isActive(Commands.get("Force1"))) {
            GameConfig.OptCreatureOutlineEnabled = true;
            _display.setMessage("Enable creature outlines.");
        }
        if (Input.get().isActive(Commands.get("Force2"))) {
            GameConfig.OptCreatureOutlineEnabled = false;
            _display.setMessage("Disable creature outlines.");
        }

        if (_testStage == 0) {
            if (InputWrapper.confirm()) {
                _testStage = 1;
                _testTime = 0;
                _catchables.clear();
                _startTime = System.currentTimeMillis();
            }
        }
        else if (_testStage == 1) {
            _display.setMessage("Creating creatures.");
            _testStage++;
        }
        else if (_testStage > 1 && _testStage < creatureCount + 1) {
            _display.setMessage("Creating creature " + (_catchables.size() + 1) + " of " + creatureCount);
            _testStage++;
            _testTime += timePassed();
            _testTime += timePassed();
            _catchables.add(new Catchable(null, null));
            _catchables.get(_catchables.size() - 1).setLocation(Screen.rand(20, 80, 20, 80));
            _catchables.get(_catchables.size() - 1).getCreature().setLocation(Screen.rand(20, 80, 20, 80));
        }
        else {
            _display.setMessage("Test run time: " + (_testTime / 1000f) + " seconds");
            _testStage = 0;
        }
    }

    private long timePassed() {
        long result = System.currentTimeMillis() - _startTime;
        _startTime = System.currentTimeMillis();
        return result;
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
    public void pause() {

    }
}
