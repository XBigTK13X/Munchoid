package game.stages.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.UserFiles;
import game.stages.common.forces.Force;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewCurrentControls extends OptionsState {
    private static class PrettyCommand {
        public final String Command;
        public final String Display;

        public PrettyCommand(String command, String display) {
            Display = display;
            Command = command;
        }
    }

    public ViewCurrentControls(Sprite background) {
        super(background);
    }

    private List<PrettyCommand> _prettyCommands;
    private Map<Command, UIButton> _buttons;
    private Map<Command, String> _chords;
    private CoolDown _delay;

    private int _columnHeight = 7;

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


    @Override
    public void create() {
        setupCommandNames();
        _buttons = new HashMap<>();
        _chords = new HashMap<>();
        _delay = new CoolDown(.1f);
        _delay.zeroOut();

        ButtonStyle style = new ButtonStyle(20, 18, 15, 10, 15);
        int ii = 0;
        for (final PrettyCommand pretty : _prettyCommands) {
            final Command command = Commands.get(pretty.Command);
            UIButton config = new UIButton(getCommandInputString(command, command.toString())) {
                @Override
                public void click() {
                    configure(command);
                }
            };
            config.getMessage().setFont("UIButton", 24);
            style.apply(config, ii / _columnHeight, _columnHeight - (ii++ % _columnHeight + 1));
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

        ButtonStyle style2 = new ButtonStyle(10, 5, 40, 10, 10);
        style2.apply(save, 0, 0);
        style2.apply(cancel, 1, 0);
    }

    private void setupCommandNames() {
        _prettyCommands = new LinkedList<>();
        _prettyCommands.add(new PrettyCommand("Confirm", "Confirm"));
        _prettyCommands.add(new PrettyCommand("MoveLeft", "Move Left"));
        _prettyCommands.add(new PrettyCommand("MoveRight", "Move Right"));
        _prettyCommands.add(new PrettyCommand("MoveUp", "Move Up"));
        _prettyCommands.add(new PrettyCommand("MoveDown", "Move Down"));
        _prettyCommands.add(new PrettyCommand("Force1", Force.values()[0] + " Force"));
        _prettyCommands.add(new PrettyCommand("Force2", Force.values()[1] + " Force"));
        _prettyCommands.add(new PrettyCommand("Force3", Force.values()[2] + " Force"));
        _prettyCommands.add(new PrettyCommand("Force4", Force.values()[3] + " Force"));
        _prettyCommands.add(new PrettyCommand("Force5", Force.values()[4] + " Force"));
        _prettyCommands.add(new PrettyCommand("Force6", Force.values()[5] + " Force"));
        _prettyCommands.add(new PrettyCommand("Help", "Show Tutorial"));
        _prettyCommands.add(new PrettyCommand("AdvanceTutorial", "Tutorial Next Page"));
        _prettyCommands.add(new PrettyCommand("Menu1", "Menu Option 1"));
        _prettyCommands.add(new PrettyCommand("Menu2", "Menu Option 2"));
        _prettyCommands.add(new PrettyCommand("Menu3", "Menu Option 3"));
        _prettyCommands.add(new PrettyCommand("Menu4", "Menu Option 4"));
        _prettyCommands.add(new PrettyCommand("Menu5", "Menu Option 5"));
        _prettyCommands.add(new PrettyCommand("Menu6", "Menu Option 6"));
        _prettyCommands.add(new PrettyCommand("Menu7", "Menu Option 7"));
        _prettyCommands.add(new PrettyCommand("Menu8", "Menu Option 8"));
        _prettyCommands.add(new PrettyCommand("Menu9", "Menu Option 9"));
        _prettyCommands.add(new PrettyCommand("Pause", "Pause"));
        _prettyCommands.add(new PrettyCommand("ToggleFullScreen", "Toggle Full Screen"));
        _prettyCommands.add(new PrettyCommand("Exit", "Exit Prompt"));
    }

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

    private String getCommandInputString(Command command, String chord) {
        for (PrettyCommand pretty : _prettyCommands) {
            if (command.name().equals(pretty.Command)) {
                return pretty.Display + "\n" + chord;
            }
        }
        return null;
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
