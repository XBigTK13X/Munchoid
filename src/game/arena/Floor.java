package game.arena;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.BackgroundMaker;
import game.GameConfig;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Logger;
import sps.core.Point2;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.entities.Entity;
import sps.util.BoundingBox;

public class Floor extends Entity {
    private static final int __fieldSmoothness = 6;
    private BoundingBox _boundingBox = BoundingBox.empty();
    Sprite _background;

    public Floor() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Floor"), DrawDepths.get("Floor"));

        if (GameConfig.OptArenaPCBBackground) {
            _background = BackgroundMaker.printedCircuitBoard(GameConfig.ArenaWidth, GameConfig.ArenaHeight);
        }
        else {
            Color dirt = Colors.rgb(55, 30, 15);
            Color grass = Colors.rgb(15, 55, 15);
            Color[][] base = ProcTextures.perlin(GameConfig.ArenaWidth, GameConfig.ArenaHeight, grass, dirt, __fieldSmoothness);
            _background = SpriteMaker.get().fromColors(base);
        }

        setSize((int) _background.getWidth(), (int) _background.getHeight());
        BoundingBox.fromDimensions(_boundingBox, 0, 0, getWidth(), getHeight());
        if (GameConfig.DevPrintArenaSize) {
            Logger.info("Arena size: (W,H): (" + _background.getWidth() + "," + _background.getHeight() + ")");
        }
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("Floor"));
    }

    public BoundingBox getBounds() {
        return _boundingBox;
    }
}
