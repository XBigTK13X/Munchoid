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
    private static Sprite __pixel;
    private static float __scaleDistance = 1;

    private Point2 _location;
    private Color _color;
    private int survivalChance = 5;

    Body _owner;
    BodyPart _container;

    private Point2 _scaledLocation = new Point2(0, 0);

    public Atom(int localX, int localY, Color color, Body owner, BodyPart container) {
        if (__pixel == null) {
            __pixel = Assets.get().pixel();
        }
        _location = new Point2(localX, localY);
        _color = color;
        _owner = owner;
        _container = container;
    }

    public void draw() {
        _scaledLocation.reset(_location.X + _location.X * (_container.getScale() * __scaleDistance), _location.Y + _location.Y * (_container.getScale() * __scaleDistance), false);
        _scaledLocation = _scaledLocation.addRaw(_owner.getPosition()).addRaw(_container.getPosition());
        Renderer.get().draw(__pixel, _scaledLocation, DrawDepths.get(Game.DrawDepths.Atom), _color, 1, 1);
    }

    public boolean isLucky() {
        return RNG.percent(survivalChance);
    }
}
