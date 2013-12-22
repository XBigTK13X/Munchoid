package sps.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.core.Logger;
import sps.core.SpsConfig;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.states.StateManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DevConsole {
    public static final int __marginPixels = 70;

    private static DevConsole __instance;

    public static DevConsole get() {
        if (__instance == null) {
            __instance = new DevConsole();
        }
        return __instance;
    }

    private final int messageLimit = 50;
    private final ConsoleText _input;
    private final ConsoleText[] _contents = new ConsoleText[messageLimit];
    private final Map<String, DevConsoleAction> _actions;
    private boolean[] _locked = new boolean[256];
    private int _index = 0;
    private boolean _isActive;
    private final Color _bgColor;
    private final Sprite _consoleBase;

    private String _fontLabel;
    private int _pointSize;

    private DevConsole() {
        _actions = new HashMap<>();
        _input = new ConsoleText(__marginPixels, Screen.get().VirtualHeight - __marginPixels, "");
        _input.getContent().setDepth(DrawDepths.get("DevConsoleText"));
        _bgColor = Color.BLACK.newAlpha(.75f);
        _consoleBase = SpriteMaker.pixel(Color.WHITE);
        _isActive = false;
        add("The development console has been started.");

        register(new DevConsoleAction("stop") {
            @Override
            public String act(int[] input) {
                toggle();
                return "";
            }
        });

        register(new DevConsoleAction("kill") {
            @Override
            public String act(int[] input) {
                Gdx.app.exit();
                return "";
            }
        });

        register(new DevConsoleAction("help") {
            @Override
            public String act(int[] input) {
                String result = "";
                for (String id : _actions.keySet()) {
                    result += id + ",";
                }
                return result;
            }
        });
    }

    public void setFont(String fontLabel, int pointSize) {
        _fontLabel = fontLabel;
        _pointSize = pointSize;
        _input.getContent().setFont(_fontLabel, _pointSize);
        _input.getContent().setMoveable(false);
        for (int ii = 0; ii < _contents.length - 1; ii++) {
            if (_contents[ii] != null) {
                _contents[ii].getContent().setFont(_fontLabel, _pointSize);
            }
        }
    }

    private int getY(int index) {
        return Screen.get().VirtualHeight - (index * __marginPixels / 4) - 200 + __marginPixels;
    }

    public void add(String message) {
        if (SpsConfig.get().devConsoleEnabled) {
            if (_index < _contents.length) {
                _contents[_index] = new ConsoleText(__marginPixels, getY(_index), message);
                _contents[_index].getContent().setFont(_fontLabel, _pointSize);
                _contents[_index].getContent().setMoveable(false);
                _index++;
            }
            else {
                for (int ii = 0; ii < _contents.length - 1; ii++) {
                    _contents[ii].setContent(_contents[ii + 1].getContent().getMessage());
                }
                _contents[_contents.length - 1].setContent(message);
            }
        }
    }

    public void draw() {
        if (_isActive) {
            _consoleBase.setSize(Screen.get().VirtualWidth, Screen.get().VirtualHeight);
            _consoleBase.setColor(_bgColor.getGdxColor());
            _consoleBase.setPosition(0, 0);
            Window.get(true).schedule(_consoleBase, DrawDepths.get("DevConsole"));
            _input.draw();
            for (ConsoleText _content : _contents) {
                if (_content != null) {
                    _content.draw();
                }
            }
        }
    }

    public void toggle() {
        if (SpsConfig.get().devConsoleEnabled) {
            _isActive = !_isActive;
            if (!StateManager.get().isPaused() && _isActive) {
                StateManager.get().setPaused(true);
            }

            for (int ii = 0; ii < _contents.length; ii++) {
                if (_contents[ii] != null) {
                    _contents[ii].getContent().setVisible(_isActive);
                }
            }
            _input.getContent().setMessage("");
        }
    }

    public boolean isActive() {
        return _isActive;
    }

    private String getInput() {
        return _input.getContent().getMessage();
    }

    private void appendInput(String input) {
        String scrub = getInput();
        _input.getContent().setMessage(scrub + input);
    }

    public void register(DevConsoleAction action) {
        _actions.put(action.Id.toLowerCase(), action);
    }

    private void takeAction() {
        String input = getInput();
        if (input.trim().length() > 0) {
            DevParsedCommand command = new DevParsedCommand(input);
            if (_actions.containsKey(command.Id.toLowerCase())) {
                String result = _actions.get(command.Id.toLowerCase()).act(command.Arguments);
                if (!result.isEmpty()) {
                    add(result);
                }
            }
            else {
                add("Unknown command: " + input);
            }
        }
        _input.getContent().setMessage("");
    }

    public void update() {
        if (_isActive) {
            try {
                for (Field keys : Input.Keys.class.getFields()) {
                    int key = keys.getInt(null);
                    if (key != Input.Keys.ANY_KEY) {
                        boolean pressed = Gdx.input.isKeyPressed(key);
                        if (pressed && !_locked[key]) {
                            if (key == Input.Keys.ENTER) {
                                if (!_input.getContent().getMessage().isEmpty()) {
                                    takeAction();
                                }
                            }
                            else if (key == Input.Keys.SPACE) {
                                appendInput(" ");
                            }
                            else if (key == Input.Keys.BACKSPACE || key == Input.Keys.DEL) {
                                _input.getContent().setMessage(_input.getContent().getMessage().substring(0, _input.getContent().getMessage().length() - 1));
                            }
                            else {
                                //Only deal with single characters
                                String chars = Input.Keys.toString(key);
                                if (chars.length() == 1) {
                                    if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                                        chars = chars.toLowerCase();
                                    }
                                    appendInput(chars);
                                }
                            }
                            _locked[key] = true;
                        }
                        else if (!pressed) {
                            _locked[key] = false;
                        }
                    }
                }
            }
            catch (Exception swallow) {
                Logger.exception(swallow);
            }
        }
    }
}
