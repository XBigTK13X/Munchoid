package game.creatures;

import game.GameConfig;
import game.forces.Force;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.graphics.Assets;
import sps.graphics.Renderer;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Markov;
import sps.util.Screen;

public class Creature extends Entity {
    private static final Markov __nameGenerator = Markov.get(Assets.get().markovSeed(), 2);

    private Body _body;
    private Stats _stats;
    private int _bonusPoints;
    private Creature _opponent;
    private String _name;

    public Creature(int partCount) {
        this(new Body(partCount));
    }

    public Creature(Body body) {
        _body = body;
        _body.setOwner(this);
        _entityType = EntityTypes.get("Creature");
        _stats = new Stats();
        _name = __nameGenerator.makeWord(RNG.next(6, 10));
        _name = _name.substring(0, 1).toUpperCase() + _name.substring(1);
    }

    public Creature(boolean faceLeft, Point2 minDimensions, Point2 maxDimensions) {
        this(new Body(RNG.next(GameConfig.MinBodyParts, GameConfig.MaxBodyParts), (int) minDimensions.X, (int) minDimensions.Y, (int) maxDimensions.X, (int) maxDimensions.Y));
        orientX(faceLeft, true);
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
                Point2 rightSide = Screen.pos(60, 20);
                setLocation(rightSide);
            }
            else {
                Point2 leftSide = Screen.pos(20, 20);
                setLocation(leftSide);
            }
        }
    }

    public void draw() {
        if (Renderer.get().isInView(getLocation().X, getLocation().Y)
                || Renderer.get().isInView(getLocation().X + _body.getWidth(), getLocation().Y)
                || Renderer.get().isInView(getLocation().X, getLocation().Y + _body.getHeight())
                || Renderer.get().isInView(getLocation().X + _body.getWidth(), getLocation().Y + _body.getHeight())) {
            _body.draw();
        }
    }

    public void update() {
        _body.update();
        useBonus();
    }

    public void attack(Force force) {
        if (_opponent != null && _opponent.getBody().isAlive()) {
            int weakness = _opponent.getStats().get(Force.weakness(force));
            int strength = _opponent.getStats().get(Force.strength(force));


            if (strength != 0) {
                String result;
                if (strength >= weakness) {
                    result = "STRONG";
                }
                else {
                    result = "WEAK";
                }
                TextPool.get().write(result, _opponent.getLocation(), 1f, TextEffects.Fountain);
            }


            int magnitude = _stats.get(force) + strength - weakness;
            if (magnitude < 1) {
                magnitude = 1;
            }

            Force.create(force, magnitude).apply(_opponent.getBody().getRandomPart());
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
            Force bonus = _stats.randomEnabledForce();
            _stats.set(bonus, _stats.get(bonus) + GameConfig.BonusAmount);

            TextPool.get().write("BONUS!", Screen.rand(40, 60, 40, 60), 2f, TextEffects.Fountain);
        }
    }

    public void setOpponent(Creature creature) {
        _opponent = creature;
    }

    public void reset(Creature source) {
        _body = source.getBody();
        _body.setOwner(this);
        _stats = source.getStats();
        _bonusPoints = source._bonusPoints;
        _name = source.getName();
    }

    public String getName() {
        return _name;
    }
}
