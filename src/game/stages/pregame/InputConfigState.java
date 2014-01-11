package game.stages.pregame;

import com.badlogic.gdx.InputProcessor;
import game.app.core.UserFiles;
import org.apache.commons.io.FileUtils;
import sps.bridge.Command;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.display.Screen;
import sps.io.Input;
import sps.io.InputBindings;
import sps.io.Keys;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import targets.DesktopGame;

import java.util.HashMap;
import java.util.Map;

public class InputConfigState implements State {
    private Text _command;
    private Text _overlap;
    private int _commandIndex = 0;
    private Command _current;

    private Map<Integer, Integer> _duplicates;

    private InputProcessor keyboardCatcher = new InputProcessor() {
        @Override
        public boolean keyDown(int i) {
            if (!_duplicates.containsKey(i)) {
                _current.bind(_current.controllerInput(), Keys.find(i));
                selectNextCommand();
                _duplicates.put(i, i);
                _overlap.setVisible(false);
                return false;
            }
            _overlap.setVisible(true);
            return false;
        }

        @Override
        public boolean keyUp(int i) {
            return false;
        }

        @Override
        public boolean keyTyped(char c) {
            return false;
        }

        @Override
        public boolean touchDown(int i, int i2, int i3, int i4) {
            return false;
        }

        @Override
        public boolean touchUp(int i, int i2, int i3, int i4) {
            return false;
        }

        @Override
        public boolean touchDragged(int i, int i2, int i3) {
            return false;
        }

        @Override
        public boolean mouseMoved(int i, int i2) {
            return false;
        }

        @Override
        public boolean scrolled(int i) {
            return false;
        }

        ;
    };

    private InputProcessor originalInputProcessor;


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
            DesktopGame.get().getInput().setInputProcessor(originalInputProcessor);
            Input.enable();
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

        Input.disable();

        originalInputProcessor = DesktopGame.get().getInput().getInputProcessor();
        DesktopGame.get().getInput().setInputProcessor(keyboardCatcher);
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

