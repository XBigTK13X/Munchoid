package game.creatures;

import game.Shared;
import game.forces.Force;
import game.states.MergeState;
import sps.bridge.Commands;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.StateManager;
import sps.util.Screen;

public class Creature extends Entity {
    private Body _body;
    private Stats _stats;

    public Creature(boolean faceLeft) {
        _body = new Body(this, RNG.next(3, 7), 200, 200);
        if (faceLeft) {
            setLocation(Screen.pos(80, 20));
        }
        else {
            setLocation(Screen.pos(20, 20));
        }

        _stats = new Stats();
    }

    public void draw() {
        _body.draw();
    }

    public void update() {
        if (Input.get().isActive(Commands.get("Force"), 0)) {
            Force.createRandom().apply(_body.getRandomPart());
        }

        _body.update();
        if (!_body.isAlive()) {
            if (Shared.get().playerCreature() == this) {

            }
            else {
                EntityManager.get().removeEntity(this);
                StateManager.get().pop();
                StateManager.get().push(new MergeState(this));
            }
        }
    }

    public Stats getStats() {
        return _stats;
    }

    public void setStats(Stats stats) {
        _stats = stats;
    }
}
