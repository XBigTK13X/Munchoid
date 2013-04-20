package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.Game;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.core.RNG;
import sps.graphics.Assets;
import sps.graphics.Renderer;

public class Atom {
    public static int count = 0;

    private static Point2 __point = new Point2(0, 0);

    private static Sprite __pixel;
    private static float __scaleDistance = 1;

    private Color _color;
    private int survivalChance = 5;

    Body _body;
    BodyPart _bodyPart;

    private float _localX;
    private float _localY;

    private float _scaledX;
    private float _scaledY;

    public Atom(int localX, int localY, Color color, Body owner, BodyPart container) {
        count++;
        if (__pixel == null) {
            __pixel = Assets.get().pixel();
        }
        _localX = localX;
        _localY = localY;
        _color = color;
        _body = owner;
        _bodyPart = container;
    }

    public void draw() {
        if (_bodyPart.getScale() < 1) {
            _scaledX = _localX - (_localX * ((1 - _bodyPart.getScale()) * __scaleDistance));
            _scaledY = _localY - (_localY * ((1 - _bodyPart.getScale()) * __scaleDistance));
        }
        else {
            _scaledX = _localX + _localX * (_bodyPart.getScale() - 1 * __scaleDistance);
            _scaledY = _localY + _localY * (_bodyPart.getScale() - 1 * __scaleDistance);
        }
        _scaledX += _body.getOwner().getLocation().X + _bodyPart.getPosition().X;
        _scaledY += _body.getOwner().getLocation().Y + _bodyPart.getPosition().Y;
        Renderer.get().draw(__pixel, __point.reset(_scaledX, _scaledY, false), DrawDepths.get(Game.DrawDepths.Atom), _color, 1, 1);
    }

    public boolean isLucky() {
        return RNG.percent(survivalChance);
    }
}
