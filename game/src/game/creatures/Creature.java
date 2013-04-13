package game.creatures;

import sps.entities.Entity;

public class Creature extends Entity {
    private Body body;

    public Creature() {
        body = new Body(5, 100, 100);
    }

    public void draw() {
        body.draw();
    }
}
