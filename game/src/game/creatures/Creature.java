package game.creatures;

import game.forces.Force;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Screen;

public class Creature extends Entity {
    private static int count = 0;

    private static final Point2 __minDimensions = Screen.pos(15, 15);
    private static final Point2 __maxDimensions = Screen.pos(40, 40);

    private static final int __bonusAmount = 1;
    private static final int __bonusAward = 3;
    private Body _body;
    private Stats _stats;
    private int _bonusPoints;
    private Creature _opponent;

    public Creature(boolean faceLeft, Point2 minDimensions, Point2 maxDimensions) {
        count++;
        Logger.info("COUNT: " + count);
        _body = new Body(this, RNG.next(3, 7), (int) minDimensions.X, (int) minDimensions.Y, (int) maxDimensions.X, (int) maxDimensions.Y);
        if (faceLeft) {
            setLocation(Screen.pos(80, 20));
        }
        else {
            setLocation(Screen.pos(20, 20));
        }

        _stats = new Stats();
    }

    public Creature(boolean facingLeft) {
        this(facingLeft, __minDimensions, __maxDimensions);
    }

    public void draw() {
        _body.draw();
    }

    public void update() {
        _body.update();
        useBonus();
    }

    public void attack() {
        if (_opponent != null && _opponent.getBody().isAlive()) {
            Force.createRandom().apply(_opponent.getBody().getRandomPart());
        }
    }

    public Stats getStats() {
        return _stats;
    }

    public void setStats(Stats stats) {
        _stats = stats;
    }

    public Body getBody() {
        return _body;
    }

    public boolean isLargerThan(Creature target) {
        return _stats.power() >= target.getStats().power();
    }

    public void addBonus(int bonus) {
        _bonusPoints += bonus;
    }

    public void useBonus() {
        while (_bonusPoints > __bonusAward) {
            _bonusPoints -= __bonusAward;
            Force bonus = Force.random();
            _stats.set(bonus, _stats.get(bonus) + __bonusAmount);
            TextPool.get().write("BONUS!", Screen.rand(40, 60, 40, 60), 2f, TextEffects.Fountain);
        }
    }

    public void setOpponent(Creature creature) {
        _opponent = creature;
    }
}
