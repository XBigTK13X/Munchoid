package game.creatures;

import game.GameConfig;
import game.forces.Force;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Screen;

public class Creature extends Entity {
    private Body _body;
    private Stats _stats;
    private int _bonusPoints;
    private Creature _opponent;

    public Creature(boolean faceLeft, Point2 minDimensions, Point2 maxDimensions) {
        _body = new Body(this, RNG.next(3, 7), (int) minDimensions.X, (int) minDimensions.Y, (int) maxDimensions.X, (int) maxDimensions.Y);
        _entityType = EntityTypes.get("Creature");
        orientX(faceLeft, true);
        _stats = new Stats();
    }

    public Creature(boolean faceLeft) {
        this(faceLeft, GameConfig.MinBodyPartSize, GameConfig.MaxBodyPartSize);
    }

    public Creature() {
        this(true, GameConfig.MinBodyPartSize, GameConfig.MaxBodyPartSize);
    }

    public void orientX(boolean faceLeft, boolean updatePos) {
        if (faceLeft != _body.isFlipX()) {
            _body.flipX(faceLeft);
        }
        if (updatePos) {
            if (faceLeft) {
                setLocation(Screen.pos(80, 20));
            } else {
                setLocation(Screen.pos(20, 20));
            }
        }
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
            Force.create(force, _stats.get(force)).apply(_opponent.getBody().getRandomPart());
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
        while (_bonusPoints > GameConfig.BonusAward) {
            _bonusPoints -= GameConfig.BonusAward;
            Force bonus = _stats.nonZeroForce();
            _stats.set(bonus, _stats.get(bonus) + GameConfig.BonusAmount);

            TextPool.get().write("BONUS!", Screen.rand(40, 60, 40, 60), 2f, TextEffects.Fountain);
        }
    }

    public void setOpponent(Creature creature) {
        _opponent = creature;
    }
}
