package game.battle;

import game.creatures.Creature;
import sps.display.Screen;
import sps.text.Text;
import sps.text.TextPool;

public class BattleHUD {
    private Creature _owner;

    private ForcesHUD _forces;
    private HealthMeter _health;
    private EnergyMeter _energy;
    private CostMeter _cost;
    private EffectHUD _effects;

    private Text _coolDown;


    public BattleHUD(Creature owner) {
        _owner = owner;

        TextPool.get().write(_owner.getName(), Screen.pos(0, 50).add((int) _owner.getLocation().X, 0));

        _forces = new ForcesHUD(owner);
        _health = new HealthMeter(owner);
        _energy = new EnergyMeter(owner);
        _cost = new CostMeter(owner);
        _coolDown = TextPool.get().write(coolDownText(owner), Screen.pos(0, 15).add((int) _owner.getLocation().X, 0));
        _effects = new EffectHUD(owner);
    }

    public void update() {
        _health.update();
        _energy.update();
        _coolDown.setMessage(coolDownText(_owner));
        _coolDown.setVisible(_owner.getCoolDown().getSecondsLeft() != 0);
    }

    public void draw() {
        _forces.draw();
        _health.draw();
        _energy.draw();
        _cost.draw();
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
