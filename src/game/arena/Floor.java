package game.arena;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.BackgroundMaker;
import game.DevConfig;
import game.GameConfig;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.color.Color;
import sps.color.RGBA;
import sps.core.Logger;
import sps.core.Point2;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.entities.Entity;
import sps.util.BoundingBox;

public class Floor extends Entity {
    private static final int __fieldSmoothness = 6;
    private BoundingBox _boundingBox = BoundingBox.empty();
    private Sprite _background;

    public Floor() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Floor"), DrawDepths.get("Floor"));
        setSize(GameConfig.ArenaWidth(), GameConfig.ArenaHeight());

        int scaleWidth = (int) (getWidth() * GameConfig.OptGraphicsDetailScale);
        int scaleHeight = (int) (getHeight() * GameConfig.OptGraphicsDetailScale);

        if (GameConfig.OptArenaPCBBackground) {
            _background = GameConfig.OptSimpleBackgrounds ?
                    BackgroundMaker.radialDark(scaleWidth, scaleHeight) :
                    BackgroundMaker.printedCircuitBoard(scaleWidth, scaleHeight);
        }
        else {
            Color dirt = new RGBA(55, 30, 15).toColor();
            Color grass = new RGBA(15, 55, 15).toColor();
            Color[][] base = ProcTextures.perlin(scaleWidth, scaleHeight, grass, dirt, __fieldSmoothness);
            _background = SpriteMaker.fromColors(base);
        }
        BoundingBox.fromDimensions(_boundingBox, 0, 0, getWidth(), getHeight());
        if (DevConfig.PrintArenaSize) {
            Logger.info("Arena size: (W,H): (" + getWidth() + "," + getHeight() + ")");
        }
        _background.setSize(getWidth(), getHeight());
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("Floor"));
    }

    public BoundingBox getBounds() {
        return _boundingBox;
    }
}
