package game.arena;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.entities.Entity;
import sps.graphics.Renderer;
import sps.util.Colors;
import sps.util.SpriteMaker;

public class Floor extends Entity {
    Sprite _background;
    public final int MarginX;
    public final int MarginY;

    public Floor() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Floor"), DrawDepths.get("Floor"));
        MarginX = (GameConfig.ArenaWidth - Renderer.get().VirtualWidth) / 2;
        MarginY = (GameConfig.ArenaWidth - Renderer.get().VirtualWidth) / 2;
        _background = SpriteMaker.get().fromColors(Colors.getPerlinGrid(GameConfig.ArenaWidth, GameConfig.ArenaHeight, Colors.rgb(55, 30, 15), Colors.rgb(15, 55, 15)));
        setSize((int) _background.getWidth(), (int) _background.getHeight());
        setLocation(new Point2(-MarginX, -MarginY));
    }

    @Override
    public void update() {
        _background.setPosition(getLocation().X, getLocation().Y);
    }

    @Override
    public void draw() {
        Renderer.get().draw(_background);
    }

    @Override
    public Point2 getLocation() {
        return _location.add(Renderer.get().getXOffset(), Renderer.get().getYOffset());
    }

}
