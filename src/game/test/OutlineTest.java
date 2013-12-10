package game.test;

import game.GameConfig;
import game.InputWrapper;
import game.creatures.Creature;
import game.creatures.style.Outline;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.io.Input;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

import java.util.ArrayList;
import java.util.List;

public class OutlineTest implements State {

    boolean _testRunning;
    long _testTime;
    long _startTime;

    Text _display;

    private List<Creature> _creatures;

    @Override
    public void create() {
        _display = TextPool.get().write("Press " + Commands.get("Confirm") + " to run the test", Screen.pos(5, 50));
        _creatures = new ArrayList<Creature>();
    }

    @Override
    public void draw() {
        for (Creature c : _creatures) {
            c.draw();
        }
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Force1"))) {
            GameConfig.OptOutlineMode = Outline.Mode.Naive;
        }
        if (Input.get().isActive(Commands.get("Force2"))) {
            GameConfig.OptOutlineMode = Outline.Mode.Fast;
        }
        if (Input.get().isActive(Commands.get("Force3"))) {
            GameConfig.OptOutlineMode = Outline.Mode.None;
        }

        if (InputWrapper.confirm()) {
            if (!_testRunning) {
                _testRunning = true;
                _testTime = 0;
                _creatures.clear();
                GameConfig.setGraphicsMode(false);
                _startTime = System.currentTimeMillis();
            }
        }
        if (_testRunning) {
            _testTime += timePassed();
            for (int ii = 0; ii < 10; ii++) {
                _testTime += timePassed();
                _creatures.add(new Creature());
                _creatures.get(_creatures.size() - 1).setLocation(Screen.rand(20, 80, 20, 80));
            }
            _testRunning = false;
            _display.setMessage("Test run time: " + (_testTime / 1000f) + " seconds");
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }
}
