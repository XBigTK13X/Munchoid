package game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import sps.bridge.Command;
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

    private Text _message;
    private Sprite _sprite;
    private int _width;
    private int _height;
    private Command _command;

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
        _command = command;

        _width = (int) Screen.width(20);
        _height = (int) Screen.height(20);

        _message = TextPool.get().write(text, new Point2(0, 0));

        Color[][] base = ProcTextures.gradient(_width, _height, Color.WHITE, Color.GRAY, false);
        Outline.single(base, Color.WHITE, 3);
        _sprite = SpriteMaker.get().fromColors(base);

        Buttons.get().add(new Buttons.User() {
            @Override
            public Sprite getSprite() {
                return _sprite;
            }

            @Override
            public void onClick() {
                click();
            }
        });

        setXY(x, y);
    }

    private void setXY(int x, int y) {
        int mW = (int) _message.getBounds().width;
        int mH = (int) _message.getBounds().height;
        _message.setPosition(x + (_width - mW) / 2, y + (_height - mH) / 2 + mH);
        _sprite.setPosition(x, y);
    }

    public void setColRow(int col, int row) {
        setXY(UIButton.x(col), UIButton.y(row));
    }

    public void draw() {
        if (_command != null) {
            if (Input.get().isActive(_command)) {
                click();
            }
        }
        Window.get().draw(_sprite);
    }

    public abstract void click();
}
