package game.battle;

import game.creatures.Creature;
import game.forces.sideeffects.SideEffectType;
import sps.text.Text;
import sps.text.TextPool;
import sps.display.Screen;

public class EffectMeter {
    private Creature _creature;

    private String _lastDisplay = "";
    private Text _display;

    public EffectMeter(Creature creature) {
        _creature = creature;
        _display = TextPool.get().write(getDisplay(), _creature.getLocation().add(Screen.pos(0, -10)));
    }

    private String getDisplay() {
        _lastDisplay = "";
        for (SideEffectType e : SideEffectType.values()) {
            _lastDisplay += _creature.getSideEffects().getCount(e) + ":";
        }
        return _lastDisplay;
    }

    public void update() {
        _display.setMessage(getDisplay());
    }
}
