package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.core.RNG;
import sps.graphics.Assets;
import sps.util.Screen;

public class Atom {
    private static Point2 __point = new Point2(0, 0);
    private static Sprite __pixel;
    public static int count = 0;
    private static float __scaleDistance = 1f;
    Body _body;
    BodyPart _bodyPart;
    private Color _color;
    private int survivalChance = 5;
    private float _localX;
    private float _localY;
    private float _scaledX;
    private float _scaledY;
    private boolean _isActive = true;

    public Atom(int localX, int localY, Color color, Body owner, BodyPart container) {
        if (__pixel == null) {
            __pixel = Assets.get().pixel();
        }
        count++;
        _localX = localX;
        _localY = localY;
        _color = color;
        _body = owner;
        _bodyPart = container;
    }

    public void draw() {
        float flipX = _body.isFlipX() ? -1f : 1f;
        float localX = _localX * flipX;

        _scaledX = localX * (_bodyPart.getScale() * __scaleDistance);
        _scaledY = _localY * (_bodyPart.getScale() * __scaleDistance);

        _scaledX += _body.getOwner().getLocation().X + (flipX * _bodyPart.getPosition().X) * _bodyPart.getScale();
        _scaledY += _body.getOwner().getLocation().Y + _bodyPart.getPosition().Y * _bodyPart.getScale();

        Screen.get().setPixel((int) _scaledX, (int) _scaledY, _color);

        //This uses OpenGL immediate mode, very fast hitting the GPU, but iterating over 3 million atoms is still slow
        // Also, it doesn't seem possible to interleave rendering atoms at the correct depth
        //RawPixels.get().setPixel((int) _scaledX, (int) _scaledY, _color);

        //This method relied on a 1x1 "pixel" sprite
        //Renderer.get().draw(__pixel, __point.reset(_scaledX, _scaledY, false), DrawDepths.get(Game.DrawDepths.Atom), _color, 1, 1);
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean active) {
        _isActive = active;
    }

    public boolean isLucky() {
        return RNG.percent(survivalChance);
    }

    public void setColor(Color color) {
        _color = color;
    }

    public Color getColor() {
        return _color;
    }
}
