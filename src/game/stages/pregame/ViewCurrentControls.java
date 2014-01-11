package game.stages.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.UserFiles;
import org.apache.commons.io.FileUtils;
import sps.bridge.Command;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.io.InputBindings;
import sps.io.KeyCatcher;
import sps.io.Keys;
import sps.states.StateManager;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;
import sps.util.CoolDown;

import java.util.HashMap;
import java.util.Map;

public class ViewCurrentControls extends OptionsState {
    public ViewCurrentControls(Sprite background) {
        super(background);
    }

    private Map<String, String> commandNames;
    private Map<Command, UIButton> _buttons;
    private Map<Command, String> _chords;
    private CoolDown _delay;

    private void setupCommandNames() {
        commandNames = new HashMap<>();
        commandNames.put("Confirm", "Confirm");
        commandNames.put("MoveLeft", "Move Left");
        commandNames.put("MoveRight", "Move Right");
        commandNames.put("MoveUp", "Move Up");
        commandNames.put("MoveDown", "Move Down");
    }

    private String _chord;
    private KeyCatcher _catcher = new KeyCatcher() {
        @Override
        public void onDown(int keyCode) {
            _chord += Keys.find(keyCode) + "+";
            updateUI();
        }

        @Override
        public void onUp(int keyCode) {
            _chord = _chord.replace(Keys.find(keyCode) + "+", "");
            updateUI();
        }
    };

    private void updateUI() {
        if (_current != null) {
            if (!_chord.isEmpty()) {
                _chords.put(_current, _chord);
                _delay.reset();
                _buttons.get(_current).setMessage(getCommandInputString(_current, "[" + getPersistableChord() + "]"));
            }
        }
    }

    private String getPersistableChord() {
        String chordFormat = _chord;
        if (chordFormat.length() > 0) {
            if (chordFormat.charAt(_chord.length() - 1) == '+') {
                chordFormat = _chord.substring(0, _chord.length() - 1);
            }
        }
        return chordFormat;
    }

    private Command _current;

    private void configure(Command command) {
        _chord = "";
        _current = command;
        _catcher.setActive(true);
    }

    @Override
    public void create() {
        setupCommandNames();
        _buttons = new HashMap<>();
        _chords = new HashMap<>();
        _delay = new CoolDown(.1f);
        _delay.zeroOut();
        ButtonStyle style = new ButtonStyle(20, 5, 60, 10, 10);
        int ii = 0;
        for (final String commandId : commandNames.keySet()) {
            final Command command = Commands.get(commandId);
            UIButton config = new UIButton(getCommandInputString(command, command.toString())) {
                @Override
                public void click() {
                    configure(command);
                }
            };
            style.apply(config, 0, 7 - ii++);
            _buttons.put(command, config);
        }

        UIButton save = new UIButton("Save") {
            @Override
            public void click() {
                for (Command command : _chords.keySet()) {
                    String chord = _chords.get(command);
                    if (chord.length() > 0) {
                        String[] keyIds = chord.split("\\+");
                        Keys[] keys = new Keys[keyIds.length];
                        int ii = 0;
                        for (String id : keyIds) {
                            keys[ii++] = Keys.get(id);
                        }
                        command.bind(command.controllerInput(), keys);
                        Logger.info("Binding " + command.name() + " to " + chord + " with a chord length " + keyIds.length);
                    }
                }
                try {
                    FileUtils.writeLines(UserFiles.input(), InputBindings.toConfig());
                }
                catch (Exception e) {
                    Logger.exception(e, false);
                }
                _catcher.setActive(false);
                StateManager.get().pop();
            }
        };

        UIButton cancel = new UIButton("Cancel") {
            @Override
            public void click() {
                if (UserFiles.input().exists()) {
                    InputBindings.init(UserFiles.input());
                }
                else {
                    InputBindings.init();
                }
                _catcher.setActive(false);
                StateManager.get().pop();
            }
        };

        ButtonStyle style2 = new ButtonStyle(5, 5, 40, 10, 10);
        style2.apply(save, 0, 1);
        style2.apply(cancel, 1, 1);
    }

    private String getCommandInputString(Command command, String chord) {
        return commandNames.get(command.name()) + ": " + chord;
    }

    @Override
    public void update() {
        if (!_delay.isCooled()) {
            if (_delay.updateAndCheck()) {
                _delay.zeroOut();
                _catcher.setActive(false);
            }
        }
    }
}
