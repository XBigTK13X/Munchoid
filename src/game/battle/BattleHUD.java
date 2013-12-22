package game.battle;

import game.creatures.Creature;
import sps.core.Point2;
import sps.display.Screen;
import sps.text.TextPool;
import sps.util.Maths;

public class BattleHUD {
    private Creature _owner;

    private ForcesHUD _forces;
    private HealthMeter _health;
    private EnergyMeter _energy;
    private CostMeter _cost;
    private EffectHUD _effects;

    private TimerGraphic _coolDown;


    public BattleHUD(Creature owner) {
        _owner = owner;

        TextPool.get().write(_owner.getName(), Screen.pos(0, 10).add((int) _owner.getLocation().X, 0));

        _forces = new ForcesHUD(owner);
        _health = new HealthMeter(owner);
        _energy = new EnergyMeter(owner);
        _cost = new CostMeter(owner);
        Point2 cooldownPos = _forces.getOrigin().add(Screen.width(12), -Screen.height(10));
        _coolDown = new TimerGraphic(false, cooldownPos, _owner.getBody().getColor());
        _effects = new EffectHUD(owner);
    }

    public void update() {
        _health.update();
        _energy.update();
        int coolDownPercent = (int) Maths.valueToPercent(0, _owner.getCoolDown().getSecondsMax(), _owner.getCoolDown().getSecondsLeft());
        _coolDown.setPercent(coolDownPercent);
        _coolDown.setVisible(!_owner.getCoolDown().isCooled());
    }

    public void draw() {
        _forces.draw();
        _health.draw();
        _energy.draw();
        _cost.draw();
        _coolDown.draw();
    }

    private String coolDownText(Creature creature) {
        return String.format("%.2f", creature.getCoolDown().getSecondsLeft()) + " sec";
    }

    public void updateAttackBasedState() {
        _effects.update();
    }

    public void flashCost(int costPercent) {
        _cost.scaleHeight(costPercent);
        _cost.show();
    }
}
