package game.creatures;

import game.forces.*;
import sps.bridge.Commands;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.io.Input;

public class Creature extends Entity {
    private Body body;

    public Creature() {
        body = new Body(RNG.next(3, 7), 200, 200);
    }

    public void draw() {
        body.draw();
    }

    public void update() {
        if (Input.get().isActive(Commands.get("Force"), 0)) {
            BodyPart part = body.getRandomPart();
            Force force = null;
            int rand = RNG.next(0, 6);
            switch (rand) {
                case 0:
                    force = new TensionForce();
                    break;
                case 1:
                    force = new ExpansiveForce();
                    break;
                case 2:
                    force = new ExplosiveForce();
                    break;
                case 3:
                    force = new SliceForce();
                    break;
                case 4:
                    force = new VaporizeForce();
                    break;
                case 5:
                    force = new AbrasiveForce();
                    break;

            }
            force.apply(part);
        }

        body.update();
        if (!body.isAlive()) {
            EntityManager.get().removeEntity(this);
            EntityManager.get().addEntity(new Creature());
        }
    }
}
