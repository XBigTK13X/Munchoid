package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class ForceMeter {
    private Force _force;
    private Creature _owner;
    private Sprite _sprite;
    private int _height;
    private int _width;

    public ForceMeter(Force force, Creature owner, int width, int height) {
        _force = force;
        _owner = owner;
        _width = (int) Screen.width(width);
        _height = (int) Screen.height(height);
        Color[][] base = new Color[width][height];
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = force.Color;
            }
        }
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);
    }

    public void draw(Point2 origin, int row) {
        Renderer.get().draw(_sprite, new Point2(origin.X, row * _height - _height + origin.Y), DrawDepths.get("ForceMeter"), Color.WHITE, _width, _height);
    }
}
