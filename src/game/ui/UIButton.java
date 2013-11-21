package game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import sps.bridge.Command;
import sps.bridge.DrawDepth;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.io.Input;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Buttons;

public abstract class UIButton {
    private static final Point2 __default = Screen.pos(20, 20);
    private static final Point2 __padding = Screen.pos(1, 1);

    public static int x(int column) {
        return (int) (column * __padding.X + column * __default.X);
    }

    public static int y(int row) {
        return (int) (row * __padding.Y + row * __default.Y);
    }

    private Buttons.User _buttonUser;
    private Text _message;
    private Sprite _sprite;
    private int _width;
    private int _height;
    private Command _command;
    private boolean _visible = true;
    private DrawDepth _depth;

    public UIButton(String text) {
        this(text, 0, 0);
    }

    public UIButton(String text, Command command) {
        this(text, 0, 0, command);
    }

    public UIButton(String text, int x, int y) {
        this(text, x, y, null);
    }

    public UIButton(String text, int x, int y, Command command) {
        _depth = DrawDepths.get("UIButton");
        _command = command;

        _width = (int) Screen.width(20);
        _height = (int) Screen.height(20);

        _message = TextPool.get().write(text, new Point2(0, 0));
        _message.setFont("UIButton", 60);
        _message.setDepth(_depth);

        Color[][] base = ProcTextures.gradient(_width, _height, Color.WHITE, Color.GRAY, false);
        Outline.single(base, Color.WHITE, 3);
        _sprite = SpriteMaker.get().fromColors(base);

        _buttonUser = new Buttons.User() {
            @Override
            public Sprite getSprite() {
                return _sprite;
            }

            @Override
            public void onClick() {
                click();
            }
        };
        Buttons.get().add(_buttonUser);

        setXY(x, y);

        setMessage(text);
    }

    public void setMessage(String message) {
        int tries = 5;
        _message.setMessage(message);
        while (_width < _message.getBounds().width && tries-- > 0) {
            _message.setMessage(_message.getMessage().replace(" ", "\n"));
            //TODO Pad each line with spaces to center
        }
    }

    private void setXY(int x, int y) {
        int mW = (int) _message.getBounds().width;
        int mH = (int) _message.getBounds().height;
        //FIXME For some reason, the centering doesn't always work.
        //Sometimes it does. For example,"Options" button is wrong,
        //   but the rest are fine
        _message.setPosition(x + ((_width - mW) / 2), y + mH + ((_height - mH) / 2));
        _sprite.setPosition(x, y);
    }

    public void setScreenPercent(int x, int y) {
        setXY((int) Screen.width(x), (int) Screen.height(y));
    }

    public void setColRow(int col, int row) {
        setXY(UIButton.x(col), UIButton.y(row));
    }

    public void draw() {
        if (_visible) {
            if (_command != null) {
                if (Input.get().isActive(_command)) {
                    click();
                }
            }
            Window.get().schedule(_sprite, _depth);
        }
    }

    public abstract void click();

    public void setVisible(boolean visible) {
        _message.setVisible(visible);
        _visible = visible;
        _buttonUser.setActive(visible);
    }

    public void setDepth(DrawDepth depth) {
        _depth = depth;
        _message.setDepth(depth);
        _buttonUser.setDepth(depth);
    }
}
