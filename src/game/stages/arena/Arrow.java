package game.stages.arena;

import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.bridge.SpriteTypes;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.display.Screen;

import java.util.List;

public class Arrow extends Entity {
    private static final float __radiusFromPlayer = Screen.height(6);
    private static final float __radiusToShow = Screen.height(20);
    private Player _owner;
    private Point2 _targetLoc = new Point2(0, 0);
    private Entity _closest;

    public Arrow(Player owner) {
        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, new Point2(0, 0), SpriteTypes.get("Arrow"), EntityTypes.get("Arrow"), DrawDepths.get("Arrow"));
        _owner = owner;
    }

    @Override
    public void update() {
        List<Entity> catchables = EntityManager.get().getEntities(EntityTypes.get("Catchable"));
        if (catchables.size() > 0) {
            _closest = catchables.get(0);
            float dist = Float.MAX_VALUE;
            Catchable c = null;
            for (Entity catchable : catchables) {
                c = (Catchable) catchable;
                if (_owner.getPet() == null || _owner.getPet().isLargerThan(c.getCreature())) {
                    if (HitTest.getDistance(this, catchable) < dist) {
                        dist = HitTest.getDistance(this, catchable);
                        _closest = catchable;
                    }
                }
            }
            double rads = Math.atan2(getLocation().Y - _closest.getLocation().Y, getLocation().X - _closest.getLocation().X);
            _graphic.setRotation(90 + (int) (rads * 180 / Math.PI));
            double x = Math.cos(rads) * -__radiusFromPlayer + _owner.getLocation().X;
            double y = Math.sin(rads) * -__radiusFromPlayer + _owner.getLocation().Y;
            _targetLoc.reset((float) x, (float) y);
            setLocation(_targetLoc);
        }
        if (HitTest.getDistance(_owner, _closest) <= __radiusToShow) {
            hide();
        }
        else {
            show();
        }
    }

    @Override
    public void draw() {
        if (_isOnBoard) {
            _graphic.draw();
        }
    }

    public Entity getClosest() {
        return _closest;
    }
}
