package game.arena;

import com.badlogic.gdx.Gdx;
import game.creatures.Body;
import game.creatures.Creature;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.bridge.SpriteTypes;
import sps.core.SpsConfig;
import sps.entities.Entity;
import sps.entities.HitTest;
import sps.util.Screen;

public class CatchNet extends Entity {

    private Player _player;
    private boolean _inUse;
    private static final float __inUseCounterSecondsMax = .15f;
    private float _inUseCounterSeconds = __inUseCounterSecondsMax;

    public CatchNet(Player player) {
        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, Screen.pos(20, 30), SpriteTypes.get("Hand"), EntityTypes.get("Hand"), DrawDepths.get("Hand"));
        _player = player;
        hide();
    }

    public void use() {
        if (!_inUse) {
            _inUse = true;
            _inUseCounterSeconds = __inUseCounterSecondsMax;
            show();
        }
    }

    public boolean isInUse() {
        return _inUse;
    }

    public void update() {
        setLocation(_player.getLocation());
        if (_inUse) {
            _inUseCounterSeconds -= Gdx.graphics.getDeltaTime();
            if (_inUseCounterSeconds <= 0) {
                disable();
            }
        }
    }

    public void disable() {
        _inUse = false;
        hide();
    }

    public boolean isTouching(Creature creature) {
        Body target = creature.getBody();

        float distanceSquared = HitTest.getDistanceSquare(
                getLocation().X + getWidth() / 2,
                creature.getLocation().X + target.getWidth() / 2,
                getLocation().Y + getHeight() / 2,
                creature.getLocation().Y + target.getHeight() / 2);
        float radiusSquared = Math.abs(((target.getWidth() / 2 + target.getHeight() / 2)) + ((getHeight() / 2 + getWidth() / 2)));
        radiusSquared /= 2;
        radiusSquared *= radiusSquared;
        boolean result = distanceSquared < radiusSquared;
        if (result) {
            disable();
        }
        return result;
    }
}
