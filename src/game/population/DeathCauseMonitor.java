package game.population;

import game.DevConfig;
import game.GameConfig;
import sps.core.Logger;
import sps.display.Screen;
import sps.text.Text;
import sps.text.TextPool;

import java.util.List;

public class DeathCauseMonitor {
    private Text _display;
    private List<DeathCause> _deathCauses;
    private String _header;
    private boolean _top;

    private int _disableIndex = 0;

    public DeathCauseMonitor(boolean top) {
        _deathCauses = DeathCauses.get().getRandom(top, GameConfig.NumberOfTournaments);
        _header = top ? "TOP" : "BOTTOM";
        _top = top;
        generateDisplay();
    }

    public void generateDisplay() {
        _display = TextPool.get().write("", _top ? Screen.pos(5, 95) : Screen.pos(75, 95));
    }

    public DeathCause disableOne() {
        DeathCause disabled = _deathCauses.get(_disableIndex++);
        disabled.setActive(false);
        return disabled;
    }

    public int totalDeaths(Population population) {
        int totalDeaths = 0;
        for (DeathCause d : _deathCauses) {
            if (d.isActive()) {
                totalDeaths += population.deathsCausedBy(d);
                if (DevConfig.DebugPopulationGrowth) {
                    Logger.info(d.getName() + " caused " + population.deathsCausedBy(d) + " deaths");
                }
            }
        }
        return totalDeaths;
    }

    public void update() {
        String tD = _header + "\n";
        for (DeathCause d : _deathCauses) {
            tD += display(d);
        }
        _display.setMessage(tD);
        _display.setScale(.5f);
    }

    private String display(DeathCause d) {
        return d.getName() + (d.isActive() ? "" : "(D)") + "\n";
    }
}
