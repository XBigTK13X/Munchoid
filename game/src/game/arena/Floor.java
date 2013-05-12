package game.arena;

import com.badlogic.gdx.graphics.Color;
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
    private static final int __fieldSmoothness = 6;
    Sprite _background;
    private final int _marginX;
    private final int _marginY;

    public Floor() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Floor"), DrawDepths.get("Floor"));
        _marginX = (GameConfig.ArenaWidth - Renderer.get().VirtualWidth) / 2;
        _marginY = (GameConfig.ArenaHeight - Renderer.get().VirtualHeight) / 2;
        Color dirt = Colors.rgb(55, 30, 15);
        Color grass = Colors.rgb(15, 55, 15);
        Color[][] base = Colors.getPerlinGrid(GameConfig.ArenaWidth, GameConfig.ArenaHeight, grass, dirt, __fieldSmoothness);
        _background = SpriteMaker.get().fromColors(base);
        setSize((int) _background.getWidth(), (int) _background.getHeight());
        setLocation(new Point2(-_marginX, -_marginY));
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

    public boolean canMoveToX(float x) {
        return x > _marginX * -2 && x < 0;
    }

    public boolean canMoveToY(float y) {
        return y > _marginY * -2 && y < 0;
    }
}
