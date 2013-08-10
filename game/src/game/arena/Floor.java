package game.arena;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Logger;
import sps.core.Point2;
import sps.draw.ProcTextures;
import sps.entities.Entity;
import sps.display.Window;
import sps.util.Bounds;
import sps.draw.Colors;
import sps.draw.SpriteMaker;

public class Floor extends Entity {
    private static final int __fieldSmoothness = 6;
    private final Bounds _bounds;
    Sprite _background;

    public Floor() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Floor"), DrawDepths.get("Floor"));
        Color dirt = Colors.rgb(55, 30, 15);
        Color grass = Colors.rgb(15, 55, 15);
        Color[][] base = ProcTextures.genPerlinGrid(GameConfig.ArenaWidth, GameConfig.ArenaHeight, grass, dirt, __fieldSmoothness);

        _background = SpriteMaker.get().fromColors(base);
        setSize((int) _background.getWidth(), (int) _background.getHeight());
        _bounds = Bounds.fromDimensions(0, 0, getWidth(), getHeight());
        if (GameConfig.DevPrintArenaSize) {
            Logger.info("Arena size: (W,H): (" + _background.getWidth() + "," + _background.getHeight() + ")");
        }
    }

    @Override
    public void draw() {
        Window.get().draw(_background);
    }

    public Bounds getBounds() {
        return _bounds;
    }
}
