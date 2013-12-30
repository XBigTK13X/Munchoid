package game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.pregame.MainMenu;
import game.save.Options;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.UIButton;
import sps.ui.UiElements;

public class ExitPrompt {
    private Color _bg;
    private Sprite _background;
    private boolean _active = false;
    private UIButton _desktop;
    private UIButton _toggleFullScreen;
    private UIButton _mainMenu;
    private UIButton _cancel;
    private Text _display;


    private static ExitPrompt __instance;
    private static State _lastState;

    public static ExitPrompt get() {
        if (_lastState != StateManager.get().current()) {
            __instance = new ExitPrompt();
            _lastState = StateManager.get().current();
        }
        if (__instance == null) {
            __instance = new ExitPrompt();
        }
        return __instance;
    }

    private ExitPrompt() {
        if (_bg == null) {
            _bg = new Color(0, 0, 0, 1);
        }
        _background = SpriteMaker.pixel(_bg);
        _background.setSize(Screen.width(100), Screen.height(100));
        _desktop = new UIButton("Desktop") {
            @Override
            public void click() {
                Gdx.app.exit();
            }
        };

        _cancel = new UIButton("No") {
            @Override
            public void click() {
                setActive(false);
            }
        };

        _toggleFullScreen = new UIButton("Toggle Fullscreen") {
            @Override
            public void click() {
                Options options = Options.load();
                options.FullScreen = !Gdx.graphics.isFullscreen();
                options.apply();
                options.save();
            }
        };

        _mainMenu = new UIButton("Main Menu") {
            @Override
            public void click() {
                StateManager.get().setPaused(false);
                if (StateManager.get().current().getClass() == MainMenu.class) {
                    _cancel.click();
                }
                else {
                    try {
                        StateManager.get().rollBackTo(MainMenu.class);
                    }
                    catch (Exception e) {
                        StateManager.reset().push(new MainMenu());
                    }
                }
            }
        };

        _desktop.setColRow(1, 2);
        _cancel.setColRow(2, 1);
        _mainMenu.setColRow(3, 2);
        _toggleFullScreen.setColRow(2, 2);

        _mainMenu.setDepth(DrawDepths.get("ExitText"));
        _desktop.setDepth(DrawDepths.get("ExitText"));
        _cancel.setDepth(DrawDepths.get("ExitText"));
        _toggleFullScreen.setDepth(DrawDepths.get("ExitText"));

        _desktop.layout();
        _cancel.layout();
        _mainMenu.layout();
        _toggleFullScreen.layout();

        _display = TextPool.get().write("Do you want to exit the game?", Screen.pos(25, 80));
        _display.setDepth(DrawDepths.get("ExitText"));

        _display.setMoveable(false);
        _desktop.setMoveable(false);
        _cancel.setMoveable(false);
        _toggleFullScreen.setMoveable(false);
        _mainMenu.setMoveable(false);
        setActive(false);
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        if (active) {
            StateManager.get().setPaused(true);
        }
        _active = active;
        _display.setVisible(active);
        _desktop.setVisible(active);
        _cancel.setVisible(active);
        _mainMenu.setVisible(active);
        _toggleFullScreen.setVisible(active);
    }

    public void updateAndDraw() {
        if (_active) {
            UiElements.get().update();
            Window.get(true).schedule(_background, DrawDepths.get("ExitBackground"));
            _display.draw();
            _desktop.draw();
            _toggleFullScreen.draw();
            _cancel.draw();
            _mainMenu.draw();
            _desktop.getMessage().draw();
            _toggleFullScreen.getMessage().draw();
            _cancel.getMessage().draw();
            _mainMenu.getMessage().draw();
        }
    }
}
