package game.stages.arena;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.BackgroundCache;
import game.config.UIConfig;
import game.app.dev.DevConfig;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Logger;
import sps.core.Point2;
import sps.display.Window;
import sps.entities.Entity;
import sps.util.BoundingBox;

public class Floor extends Entity {
    private BoundingBox _boundingBox = BoundingBox.empty();
    private Sprite _background;

    public Floor() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Floor"), DrawDepths.get("Floor"));
        setSize(UIConfig.ArenaWidth(), UIConfig.ArenaHeight());

        _background = BackgroundCache.getRandom();

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

    public void resizeSprite() {
        _background.setSize(getWidth(), getHeight());
    }
}
