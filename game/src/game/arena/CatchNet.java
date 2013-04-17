package game.arena;

import com.badlogic.gdx.Gdx;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.bridge.SpriteTypes;
import sps.entities.Entity;
import sps.util.Screen;

public class CatchNet extends Entity {

    private Player _player;
    private boolean _inUse;
    private static final float __inUseCounterSecondsMax = .4f;
    private float _inUseCounterSeconds = __inUseCounterSecondsMax;


    public CatchNet(Player player) {
        initialize(Screen.pos(20, 30), SpriteTypes.get("Hand"), EntityTypes.get("Hand"), DrawDepths.get("Hand"));
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
                _inUse = false;
                hide();
            }
        }
    }
}
