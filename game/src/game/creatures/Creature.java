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
    private static final Markov __nameGenerator = Markov.get(Assets.get().markovSeed(), 3);

    private Body _body;
    private Stats _stats;
    private int _bonusPoints;
    private Creature _opponent;
    private String _name;

    public Creature(boolean faceLeft, Point2 minDimensions, Point2 maxDimensions) {
        _body = new Body(this, RNG.next(GameConfig.MinBodyParts, GameConfig.MaxBodyParts), (int) minDimensions.X, (int) minDimensions.Y, (int) maxDimensions.X, (int) maxDimensions.Y);
        _entityType = EntityTypes.get("Creature");
        orientX(faceLeft, true);
        _stats = new Stats();
        _name = __nameGenerator.makeWord(RNG.next(6, 10));
        _name = _name.substring(0, 1).toUpperCase() + _name.substring(1);
    }

    public Creature(boolean faceLeft) {
        this(faceLeft, GameConfig.MinBodyPartSize, GameConfig.MaxBodyPartSize);
    }

    public Creature(Body body) {
        _body = body;
        _entityType = EntityTypes.get("Creature");
        _body.setOwner(this);
        _stats = new Stats();
    }

    public Creature() {
        this(true, GameConfig.MinBodyPartSize, GameConfig.MaxBodyPartSize);
    }

    public void orientX(boolean faceLeft, boolean updatePos) {
        if (faceLeft != _body.isFlipX()) {
            _body.flipX(faceLeft);
        }
        if (updatePos) {
            setLocation(faceLeft ? Screen.pos(80, 20) : Screen.pos(20, 20));
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
                TextPool.get().write(result, getLocation(), 1f, TextEffects.Fountain);
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

    public void reset(Creature source) {
        _body = source._body;
        _body.setOwner(this);
        _stats = source._stats;
        _bonusPoints = source._bonusPoints;
    }

    public String getName() {
        return _name;
    }
}
