package game.battle;

import game.creatures.Creature;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Screen;

public class BattleHUD {
    private boolean _leftSide;
    private Creature _owner;

    private ForcesHUD _forces;
    private HealthMeter _health;
    private EnergyMeter _energy;
    private EffectMeter _effects;

    private Text _coolDown;


    public BattleHUD(Creature owner, boolean leftSide) {
        _owner = owner;
        _leftSide = leftSide;

        TextPool.get().write(_owner.getName(), Screen.pos(0, 50).add((int) _owner.getLocation().X, 0));

        _forces = new ForcesHUD(owner);
        _health = new HealthMeter(owner);
        _energy = new EnergyMeter(owner);
        _coolDown = TextPool.get().write(coolDownText(owner), Screen.pos(0, 15).add((int) _owner.getLocation().X, 0));
        _effects = new EffectMeter(owner);
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
    }

    private String coolDownText(Creature creature) {
        return String.format("%.2f", creature.getCoolDown().getSecondsLeft()) + " sec";
    }

    public void updateAttackBasedState() {
        _effects.update();
    }
}
