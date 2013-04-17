package game.arena;

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

    public Catchable() {
        _creature = new Creature(true, Screen.pos(2, 2), Screen.pos(5, 5));
        setLocation(Point2.random());
    }

    @Override
    public void update() {
        _creature.setLocation(getLocation());

        CatchNet net = (CatchNet) EntityManager.get().getEntity(EntityTypes.get("Hand"));
        if (net != null && net.isInUse() && net.isTouching(_creature)) {
            Player player = (Player) EntityManager.get().getPlayers().get(0);
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
        move(RNG.next(-_moveDistance, _moveDistance), RNG.next(-_moveDistance, _moveDistance));
    }

    @Override
    public void draw() {
        _creature.draw();
    }
}
