package sps.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.io.Input;
import sps.text.Text;
import sps.text.TextPool;

import java.util.ArrayList;
import java.util.List;

public class ToolTip {
    public interface User {
        boolean isActive();

        String message();
    }

    private static final Point2 __tooltipOffset = Screen.pos(1, 1);
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
    private float _messageWidth;
    private float _messageHeight;

    private List<User> _users;

    private ToolTip() {
        if (__bg == null) {
            Color[][] tbg = ProcTextures.monotone(2, 2, new Color(.1f, .1f, .1f, .7f));
            __bg = SpriteMaker.get().fromColors(tbg);
        }
        _position = new Point2(0, 0);
        _message = TextPool.get().write("NOTHING", _position);
        _message.hide();
        _users = new ArrayList<>();
    }

    public void display(String message) {
        _message.setMessage(message);
        _messageWidth = _message.getBounds().width;
        _messageHeight = _message.getBounds().heightx;
        _position.reset(Input.get().x() + (int) __tooltipOffset.X, Input.get().y() + (int) __tooltipOffset.Y);
        _message.setPosition((int) _position.X, (int) _position.Y);
    }

    public void draw() {
        if (_active) {
            int fontWidthOffset = 0;//-__fontWidth / 2
            int fontHeightOffset = 0;//-(int) (_message.getBounds().height * .45))
            Window.get().draw(__bg, _position.add(fontWidthOffset, fontHeightOffset), DrawDepths.get("TooltipBackground"), Color.WHITE, _messageWidth, _messageHeight);
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
