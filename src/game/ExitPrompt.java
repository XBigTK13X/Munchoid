package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.ui.UIButton;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.text.Text;
import sps.text.TextPool;

public class ExitPrompt {
    private Color _bg;
    private Sprite _background;
    private boolean _active = false;
    private UIButton _confirm;
    private UIButton _cancel;
    private Text _display;

    public ExitPrompt() {
        if (_bg == null) {
            _bg = new Color(0, 0, 0, 1);
        }
        _background = SpriteMaker.pixel(_bg);
        _background.setSize(Screen.width(100), Screen.height(100));
        _confirm = new UIButton("Yes") {
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

        _confirm.setColRow(1, 1);
        _cancel.setColRow(3, 1);

        _confirm.setDepth(DrawDepths.get("ExitText"));
        _cancel.setDepth(DrawDepths.get("ExitText"));

        _display = TextPool.get().write("Do you want to exit the game?", Screen.pos(25, 60));
        _display.setDepth(DrawDepths.get("ExitText"));
        setActive(false);
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
        _display.setVisible(active);
        _confirm.setVisible(active);
        _cancel.setVisible(active);

    }

    public void update() {
        if (_active) {
            Window.get().schedule(_background, DrawDepths.get("ExitBackground"));
            _confirm.draw();
            _cancel.draw();
        }
    }
}
