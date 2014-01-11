package game.stages.battle;

import game.config.UIConfig;
import game.stages.common.creatures.Creature;
import game.stages.common.forces.sideeffects.SideEffectType;
import sps.display.Screen;
import sps.text.Text;
import sps.text.TextPool;

public class EffectHUD {
    private Creature _creature;

    private String _lastDisplay = "";
    private Text _display;

    public EffectHUD(Creature creature) {
        _creature = creature;
        _display = TextPool.get().write(getDisplay(), _creature.getLocation().add(Screen.pos(0, -10)));
    }

    private String getDisplay() {
        if (UIConfig.BattleEffectHUDEnabled) {
            _lastDisplay = "";
            for (SideEffectType e : SideEffectType.values()) {
                _lastDisplay += _creature.getSideEffects().getCount(e) + ":";
            }
        }
        return _lastDisplay;
    }

    public void update() {
        _display.setMessage(getDisplay());
    }
}
