package game.creatures;

import game.Shared;
import game.forces.Force;
import game.states.MergeState;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.StateManager;
import sps.util.Screen;

public class Creature extends Entity {
    private static final int __bonusAmount = 1;
    private static final int __bonusAward = 1000;

    private Body _body;
    private Stats _stats;
    private int _bonusPoints;

    public Creature(boolean faceLeft, Point2 minDimensions, Point2 maxDimensions) {
        _body = new Body(this, RNG.next(3, 7), (int) minDimensions.X, (int) minDimensions.Y, (int) maxDimensions.X, (int) maxDimensions.Y);
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

    public boolean isLargerThan(Creature target) {
        return _stats.power() > target.getStats().power();
    }

    public void addBonus(int bonus) {
        _bonusPoints += bonus;
    }

    public void useBonus() {
        while (_bonusPoints > __bonusAward) {
            _bonusPoints -= __bonusAward;
            Force bonus = Force.random();
            _stats.set(bonus, _stats.get(bonus) + __bonusAmount);
        }
    }
}
