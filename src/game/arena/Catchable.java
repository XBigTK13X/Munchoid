package game.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import game.DevConfig;
import game.GameConfig;
import game.creatures.Creature;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.entities.HitTest;

public class Catchable extends Entity {
    private static final int __changeDirectionSecondsMax = 3;
    public static final Color CanBeCaughtHighlight = new Color(Color.BLUE);

    private Creature _creature;
    private float _dX = 0;
    private float _dY = 0;
    private static final float __spawnBuffer = .10f;

    private float _changeDirectionsSeconds = 0;

    private float _radius;

    private static Player __player;

    public Catchable(Player player, Floor floor) {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Catchable"), DrawDepths.get("Catchable"));
        __player = player;
        _creature = new Creature();
        _creature.getBody().setFloor(floor);
        _creature.getBody().setScale(GameConfig.ArenaCreatureScale);
        _creature.orientX((DevConfig.FlipEnabled) ? RNG.coinFlip() : false, false);
        setSize(_creature.getWidth(), _creature.getHeight());
        if (floor != null) {
            setLocation(new Point2(RNG.next((int) (floor.getBounds().X2 * __spawnBuffer), (int) (floor.getBounds().X2 * (1f - __spawnBuffer))), RNG.next((int) (floor.getBounds().Y2 * __spawnBuffer), (int) (floor.getBounds().Y2 * (1f - __spawnBuffer)))));
        }
    }

    private void movement() {
        if (DevConfig.FlipEnabled) {
            _creature.orientX(_dX <= 0, false);
        }

        _changeDirectionsSeconds -= Gdx.graphics.getDeltaTime();

        boolean anyPartOutside = _creature.getBody().anyPartOutsideArena(_dX * Gdx.graphics.getDeltaTime(), _dY * Gdx.graphics.getDeltaTime());
        if (!anyPartOutside) {
            move(_dX, _dY);
        }

        //TODO Break arena into virtual tiles and only check against catchables in neighboring tiles
        _radius = Math.max(_creature.getBody().getWidth(), _creature.getBody().getHeight()) * _creature.getBody().getScale();
        Entity nearest = EntityManager.get().getNearest(this, EntityTypes.get("Catchable"));
        float dist = HitTest.getDistance(this, nearest);

        if (_changeDirectionsSeconds <= 0 || anyPartOutside || (_changeDirectionsSeconds <= 0 && dist <= _radius)) {
            _changeDirectionsSeconds = RNG.next(__changeDirectionSecondsMax / 2, __changeDirectionSecondsMax);
            float playerSpeedPercent = .5f;
            _dX = RNG.next(-GameConfig.PlayerTopSpeed, GameConfig.PlayerTopSpeed) * playerSpeedPercent;
            _dY = RNG.next(-GameConfig.PlayerTopSpeed, GameConfig.PlayerTopSpeed) * playerSpeedPercent;
        }
    }

    private void updateColor() {
        if (__player.getNet().isTouching(_creature)) {
            if (__player.getPet() == null || __player.getPet().isLargerThan(_creature)) {
                _creature.getBody().setHighlight(CanBeCaughtHighlight);
            }
            else {
                _creature.getBody().setHighlight(Color.RED);
            }
        }
        else {
            _creature.getBody().setHighlight(Color.WHITE);
        }
    }

    @Override
    public void update() {
        if (!_creature.getBody().getParts().anyAlive()) {
            setInactive();
        }
        _creature.setLocation(getLocation());

        movement();
        updateColor();
    }

    @Override
    public void draw() {
        _creature.draw();
    }

    public Creature getCreature() {
        return _creature;
    }

    public void setCreature(Creature creature) {
        _creature = creature;
        setSize(_creature.getWidth(), _creature.getHeight());
        _creature.getBody().setScale(GameConfig.ArenaCreatureScale);
    }
}
