package game.stages.pregame;

import game.app.core.UserFiles;
import org.apache.commons.io.FileUtils;
import sps.bridge.Command;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.display.Screen;
import sps.io.InputBindings;
import sps.io.KeyCatcher;
import sps.io.Keys;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.util.HashMap;
import java.util.Map;

public class InputConfigState implements State {
    private Text _command;
    private Text _overlap;
    private int _commandIndex = 0;
    private Command _current;

    private Map<Integer, Integer> _duplicates;


    private KeyCatcher keyCatcher = new KeyCatcher() {

        @Override
        public void onDown(int keyCode) {
            if (!_duplicates.containsKey(keyCode)) {
                _current.bind(_current.controllerInput(), Keys.find(keyCode));
                selectNextCommand();
                _duplicates.put(keyCode, keyCode);
                _overlap.setVisible(false);
            }
            _overlap.setVisible(true);
        }
    };

    private void setCommand() {
        if (_current != null) {
            _command.setMessage("Press the key for: " + _current.name());
        }
    }

    private void selectNextCommand() {
        if (_commandIndex < Commands.values().size()) {
            _current = Commands.values().get(_commandIndex);
            setCommand();
            _commandIndex++;
        }
        else {
            try {
                FileUtils.writeLines(UserFiles.input(), InputBindings.toConfig());
            }
            catch (Exception e) {
                Logger.exception(e, false);
            }
            keyCatcher.setActive(false);
            StateManager.get().pop();
        }
    }

    @Override
    public void create() {
        _command = TextPool.get().write("", Screen.pos(10, 70));
        _overlap = TextPool.get().write("Already in use", Screen.pos(10, 30));
        _duplicates = new HashMap<>();
        _overlap.setVisible(false);
        selectNextCommand();
        keyCatcher.setActive(true);
    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {

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

