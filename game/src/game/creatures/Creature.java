package game.creatures;

import sps.core.Logger;
import sps.entities.Entity;

public class Creature extends Entity {
    private Body body;

    public Creature() {
        Logger.info("New creature");
        body = new Body(5, 100, 100);
    }

    public void draw() {
        body.draw();
    }
}
