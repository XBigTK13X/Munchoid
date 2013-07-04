package sps.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Window;
import sps.io.Input;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

import java.util.ArrayList;
import java.util.List;

public class ToolTip {
    public interface User {
        boolean isActive();

        String message();
    }

    private static final Point2 __tooltipOffset = Screen.pos(1, 1);
    private static final int __fontWidth = 25;
    private static final int __fontHeight = __fontWidth * 4;
    private static Sprite __bg;

    private static ToolTip __instance;

    public static ToolTip get() {
        if (__instance == null) {
            __instance = new ToolTip();
        }
        return __instance;
    }

    public static void reset() {
        __instance = new ToolTip();
    }

    private Text _message;
    private Point2 _position;
    private boolean _active;
    private int _messageWidth;

    private List<User> _users;

    private ToolTip() {
        if (__bg == null) {
            Color[][] tbg = Colors.genArr(2, 2, new Color(.1f, .1f, .1f, .7f));
            __bg = SpriteMaker.get().fromColors(tbg);
        }
        _position = new Point2(0, 0);
        _message = TextPool.get().write("NOTHING", _position);
        _message.hide();
        _users = new ArrayList<User>();
    }

    public void display(String message) {
        _message.setMessage(message);
        _messageWidth = _message.getMessage().length() * __fontWidth;
        _position.reset(Input.get().x() + (int) __tooltipOffset.X, Input.get().y() + (int) __tooltipOffset.Y);
        _message.setPosition((int) _position.X, (int) _position.Y);
    }

    public void draw() {
        if (_active) {
            Window.get().draw(__bg, _position.add(-__fontWidth / 2, -(int) (__fontHeight * .45)), DrawDepths.get("TooltipBackground"), Color.WHITE, _messageWidth, __fontHeight);
        }
    }

    public void update() {
        _active = false;
        for (User user : _users) {
            if (user.isActive()) {
                _active = true;
                display(user.message());
            }
        }
        _message.setVisible(_active);
    }

    public void add(User user) {
        _users.add(user);
    }
}
