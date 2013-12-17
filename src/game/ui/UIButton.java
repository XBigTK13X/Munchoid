package game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import org.apache.commons.lang3.StringUtils;
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
    private Point2 _position;
    private Color _start;
    private Color _end;

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
        _start = Color.WHITE;
        _end = Color.GRAY;
        _depth = DrawDepths.get("UIButton");
        _command = command;
        _position = new Point2(0, 0);

        _message = TextPool.get().write(text, new Point2(0, 0));
        _message.setFont("UIButton", 60);
        _message.setDepth(_depth);

        setSize(20, 20);

        _buttonUser = new Buttons.User() {
            @Override
            public Sprite getSprite() {
                return _sprite;
            }

            @Override
            public void onClick() {
                click();
            }

            @Override
            public void onMouseDown() {
                mouseDown();
            }
        };
        Buttons.get().add(_buttonUser);

        setMessage(text);
        setXY(x, y);
    }

    public Point2 getPosition() {
        return _position;
    }

    public void setBackgroundColors(Color start, Color end) {
        _start = start;
        _end = end;
        rebuildBackground();
    }

    private void rebuildBackground() {
        Color[][] base = ProcTextures.gradient(_width, _height, _start, _end, false);
        Outline.single(base, Color.WHITE, 3);
        _sprite = SpriteMaker.get().fromColors(base);
    }

    public void setSize(int width, int height) {
        _width = (int) Screen.width(width);
        _height = (int) (Screen.height(height));

        rebuildBackground();

        setMessage(_message.getMessage());
        setXY((int) getPosition().X, (int) getPosition().Y);
    }

    public void setMessage(String message) {
        int maxLines = 5;
        _message.setMessage(message);
        int longestLineLength = 0;
        while (_width < _message.getBounds().width && maxLines-- > 0) {
            _message.setMessage(_message.getMessage().replace(" ", "\n"));
        }
        _message.setMessage(_message.getMessage().trim());
        for (String line : _message.getMessage().split("\n")) {
            if (line.length() > longestLineLength) {
                longestLineLength = line.length();
            }
        }
        for (String line : _message.getMessage().split("\n")) {
            if (line.length() < longestLineLength) {
                int offset = (longestLineLength - line.length());
                _message.setMessage(_message.getMessage().replace(line, StringUtils.repeat(" ", offset) + line));
            }
        }
        setXY((int) _position.X, (int) _position.Y);
    }

    public void setXY(int x, int y) {
        int mW = (int) _message.getBounds().width;
        int mH = (int) _message.getBounds().height;
        //FIXME For some reason, the centering doesn't always work.
        //Sometimes it does. For example,"Options" button is wrong,
        //   but the rest are fine
        int mX = x + (_width - mW) / 2;
        int mY = y + mH + (_height - mH) / 2;
        _message.setPosition(mX, mY);
        _sprite.setPosition(x, y);
        _position.reset(x, y);
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

    public void mouseDown() {

    }

    public boolean beingClicked() {
        return _buttonUser.isBeingClicked();
    }

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

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public Sprite getSprite() {
        return _sprite;
    }
}
