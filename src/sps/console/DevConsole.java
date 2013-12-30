package sps.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.display.Screen;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.MultiText;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DevConsole {
    private static final int __marginPixels = 70;
    private static final int __consoleBufferSize = 50;

    private static DevConsole __instance;

    public static DevConsole get() {
        if (__instance == null) {
            __instance = new DevConsole();
        }
        return __instance;
    }

    private final Text _input;
    private final Map<String, DevConsoleAction> _actions;
    private boolean[] _locked = new boolean[256];
    private boolean _isActive;
    private MultiText _multiText;

    private DevConsole() {
        _actions = new HashMap<>();
        _input = TextPool.get().write("", new Point2(__marginPixels, Screen.get().VirtualHeight - __marginPixels));
        _input.setDepth(DrawDepths.get("DevConsoleText"));

        _multiText = new MultiText(__consoleBufferSize, __marginPixels, Color.BLACK.newAlpha(.75f), Screen.get().VirtualWidth, Screen.get().VirtualHeight);
        _multiText.setBackgroundDepth(DrawDepths.get("DevConsole"));
        _multiText.setTextDepth(DrawDepths.get("DevConsoleText"));

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
        _input.setFont(fontLabel, pointSize);
        _input.setMoveable(false);
        _multiText.setFont(fontLabel, pointSize);
    }

    public void add(String message) {
        if (SpsConfig.get().devConsoleEnabled) {
            _multiText.add(message);
        }
    }

    public void draw() {
        if (_isActive) {
            _multiText.draw();
            _input.draw();
        }
    }

    public void toggle() {
        if (SpsConfig.get().devConsoleEnabled) {
            _isActive = !_isActive;
            if (!StateManager.get().isPaused() && _isActive) {
                StateManager.get().setPaused(true);
            }

            _multiText.setVisible(_isActive);
            _input.setMessage("");
        }
    }

    public boolean isActive() {
        return _isActive;
    }

    private String getInput() {
        return _input.getMessage();
    }

    private void appendInput(String input) {
        String scrub = getInput();
        _input.setMessage(scrub + input);
    }

    public void register(DevConsoleAction action) {
        _actions.put(action.Id.toLowerCase(), action);
    }

    private void takeAction() {
        String input = getInput();
        if (input.trim().length() > 0) {
            try {
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
            catch (Exception e) {
                add("Exception caught while attempting to parse command.");
            }
        }
        _input.setMessage("");
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
                                if (!_input.getMessage().isEmpty()) {
                                    takeAction();
                                }
                            }
                            else if (key == Input.Keys.SPACE) {
                                appendInput(" ");
                            }
                            else if (key == Input.Keys.BACKSPACE || key == Input.Keys.DEL) {
                                if (getInput().length() > 0) {
                                    _input.setMessage(_input.getMessage().substring(0, _input.getMessage().length() - 1));
                                }
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
