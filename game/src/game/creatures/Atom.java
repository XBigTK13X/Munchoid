package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.Game;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.core.RNG;
import sps.graphics.Assets;
import sps.graphics.Renderer;

//TODO Improve performance, currently get ~20-30 creatures manageable per 512MB of available memory
public class Atom {
    public static int count = 0;

    private static Sprite __pixel;
    private static float __scaleDistance = 1;

    private Point2 _location;
    private Color _color;
    private int survivalChance = 5;


    Body _body;
    BodyPart _bodyPart;

    private Point2 _scaledLocation = new Point2(0, 0);

    public Atom(int localX, int localY, Color color, Body owner, BodyPart container) {
        count++;
        if (__pixel == null) {
            __pixel = Assets.get().pixel();
        }
        _location = new Point2(localX, localY);
        _color = color;
        _body = owner;
        _bodyPart = container;
    }

    public void draw() {
        if (_bodyPart.getScale() < 1) {
            _scaledLocation.reset(_location.X - (_location.X * ((1 - _bodyPart.getScale()) * __scaleDistance)), _location.Y - (_location.Y * ((1 - _bodyPart.getScale()) * __scaleDistance)), false);
        }
        else {
            _scaledLocation.reset(_location.X + _location.X * (_bodyPart.getScale() - 1 * __scaleDistance), _location.Y + _location.Y * (_bodyPart.getScale() - 1 * __scaleDistance), false);
        }
        _scaledLocation = _scaledLocation.addRaw(_body.getOwner().getLocation()).addRaw(_bodyPart.getPosition());
        Renderer.get().draw(__pixel, _scaledLocation, DrawDepths.get(Game.DrawDepths.Atom), _color, 1, 1);
    }

    public boolean isLucky() {
        return RNG.percent(survivalChance);
    }
}
