package game.creatures;

import game.forces.AbrasiveForce;
import game.forces.Force;
import sps.bridge.Commands;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.io.Input;

public class Creature extends Entity {
    private Body body;

    public Creature() {
        body = new Body(5, 200, 200);
    }

    public void draw() {
        body.draw();
    }

    public void update() {
        if (Input.get().isActive(Commands.get("Force"), 0)) {
            BodyPart part = body.getRandomPart();
            Force force = new AbrasiveForce();
            force.apply(part);
        }

        body.update();
        if (!body.isAlive()) {
            EntityManager.get().removeEntity(this);
            EntityManager.get().addEntity(new Creature());
        }
    }
}
