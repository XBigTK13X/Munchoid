package game.creatures;

import game.GameConfig;
import game.forces.Force;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Screen;

public class Creature extends Entity {


    private static final int __bonusAmount = 1;
    private static final int __bonusAward = 3;
    private Body _body;
    private Stats _stats;
    private int _bonusPoints;
    private Creature _opponent;

    public Creature(boolean faceLeft, Point2 minDimensions, Point2 maxDimensions) {
        _body = new Body(this, RNG.next(3, 7), (int) minDimensions.X, (int) minDimensions.Y, (int) maxDimensions.X, (int) maxDimensions.Y);
        if (faceLeft) {
            Logger.info("LEFT");
            setLocation(Screen.pos(80, 20));
            _body.flipX();
        }
        else {
            Logger.info("RIGHT");
            setLocation(Screen.pos(20, 20));
        }

        _stats = new Stats();
    }

    public Creature(boolean faceLeft) {
        this(faceLeft, GameConfig.MinBodyPartDimensions, GameConfig.MaxBodyPartDimensions);
    }

    public Creature() {
        this(true, GameConfig.MinBodyPartDimensions, GameConfig.MaxBodyPartDimensions);
    }

    public void draw() {
        _body.draw();
    }

    public void update() {
        _body.update();
        useBonus();
    }

    public void attack(Force force) {
        if (_opponent != null && _opponent.getBody().isAlive()) {
            Force.create(force).apply(_opponent.getBody().getRandomPart());
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
