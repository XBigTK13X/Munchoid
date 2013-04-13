package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.Game;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Assets;
import sps.graphics.Renderer;

public class Atom {
    protected static Sprite __pixel;

    private Point2 _location;
    private Color _color;

    Body _owner;
    BodyPart _container;

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
        Renderer.get().draw(__pixel, _location.addRaw(_owner.getPosition()).addRaw(_container.getPosition()), DrawDepths.get(Game.DrawDepths.Atom), _color, 1, 1);
    }
}
