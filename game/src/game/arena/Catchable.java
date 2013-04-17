package game.arena;

import game.Shared;
import game.creatures.Creature;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.util.Screen;

public class Catchable extends Entity {
    private Creature _creature;

    public Catchable() {
        _creature = new Creature(true, Screen.pos(2, 2), Screen.pos(5, 5));
        setLocation(Point2.random());
    }

    @Override
    public void update() {
        _creature.setLocation(getLocation());

        CatchNet net = (CatchNet) EntityManager.get().getEntity(EntityTypes.get("Hand"));
        if (net != null && HitTest.isTouching(_creature, net) && net.isInUse()) {
            Creature pet = Shared.get().playerCreature();
            if (pet == null) {
                Shared.get().setPlayerCreature(_creature);
            }
            else {
                if (pet.isLargerThan(_creature)) {
                    pet.addBonus(pet.getStats().power() - _creature.getStats().power());
                }
            }
            setInactive();
        }
    }

    @Override
    public void draw() {
        _creature.draw();
    }
}
