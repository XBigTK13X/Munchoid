package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class ForceMeter {
    private static Sprite _background;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private int _scaledWidth;


    public ForceMeter(Force force, Creature owner, int width, int height) {
        _width = (int) Screen.width(width);
        _height = (int) Screen.height(height);
        _scaledWidth = (int) ((owner.getStats().get(force) / (float) GameConfig.MaxStat) * _width);

        if (_background == null) {
            Color[][] bg = Colors.genArr(width, height, Color.LIGHT_GRAY);
            _background = SpriteMaker.get().fromColors(bg);
        }

        Color[][] base = Colors.genArr(width, height, force.Color);
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);
    }

    public void draw(Point2 origin, int row) {
        Point2 position = new Point2(origin.X, row * _height - _height + origin.Y);
        Renderer.get().draw(_background, position, DrawDepths.get("ForceMeter"), Color.WHITE, _width, _height);
        Renderer.get().draw(_sprite, position, DrawDepths.get("ForceMeter"), Color.WHITE, _scaledWidth, _height);
    }
}
