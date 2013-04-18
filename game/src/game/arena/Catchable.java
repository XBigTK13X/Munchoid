package game.arena;

import com.badlogic.gdx.Gdx;
import game.creatures.Creature;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.util.Screen;

public class Catchable extends Entity {
    private Creature _creature;
    private int _moveDistance = (int) (Screen.height(1) + Screen.width(1)) / 2;

    private float _dX = 0;
    private float _dY = 0;

    private static final float __changeDriectionSecondsMax = 4.5f;
    private float _changeDirectionSeconds;

    public Catchable() {
        _creature = new Creature(true, Screen.pos(2, 2), Screen.pos(5, 5));
        setSize(_creature.getWidth(), _creature.getHeight());
        setLocation(Point2.random());
    }

    @Override
    public void update() {
        _creature.setLocation(getLocation());

        CatchNet net = (CatchNet) EntityManager.get().getEntity(EntityTypes.get("Hand"));
        Player player = (Player) EntityManager.get().getPlayers().get(0);

        if (player.getPet() != null) {
            if (player.getPet().getStats().power() >= _creature.getStats().power()) {
                //TODO Indicate that it can be eaten
                //Flashing is built into _graphic, can we write the arrays to the sprite?
            }
        }

        if (net != null && net.isInUse() && net.isTouching(_creature)) {
            if (player.getPet() == null) {
                player.setPet(_creature);
                setInactive();
            }
            else {
                if (player.getPet().isLargerThan(_creature)) {
                    player.getPet().addBonus(player.getPet().getStats().power() - _creature.getStats().power());
                    setInactive();
                }
                else {
                    _creature.addBonus(_creature.getStats().power() - player.getPet().getStats().power());
                }
            }
        }
        if (_changeDirectionSeconds <= 0) {
            _changeDirectionSeconds = RNG.next(0, (int) __changeDriectionSecondsMax * 100) / 100f;
            Point2 dest = Screen.rand(10, 90, 10, 90);
            double direction = Math.atan2(dest.Y - getLocation().Y, dest.X - getLocation().X);
            _dX = (float) Math.cos(direction) * _moveDistance;
            _dY = (float) Math.sin(direction) * _moveDistance;
        }
        _changeDirectionSeconds -= Gdx.graphics.getDeltaTime();
        move(_dX, _dY);
    }

    @Override
    public void draw() {
        _creature.draw();
    }
}
