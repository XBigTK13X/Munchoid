package game.stages.population;

import game.app.config.GameConfig;
import game.app.config.UIConfig;
import game.app.dev.DevConfig;
import sps.core.Logger;
import sps.display.Screen;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Parse;

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
        if (UIConfig.UseOldPopulationDeathMonitors) {
            _display = TextPool.get().write("", _top ? Screen.pos(75, 95) : Screen.pos(75, 45));
        }
    }

    public DeathCause disableOne() {
        DeathCause disabled = _deathCauses.get(_disableIndex++);
        disabled.setActive(false);
        return disabled;
    }

    public int getActiveCount() {
        int active = 0;
        for (DeathCause cause : _deathCauses) {
            active += cause.isActive() ? 1 : 0;
        }
        return active;
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
        if (UIConfig.UseOldPopulationDeathMonitors) {
            String tD = _header + "\n";
            for (DeathCause d : _deathCauses) {
                tD += display(d);
            }
            _display.setMessage(tD);
            _display.setScale(.5f);
        }
    }

    private String display(DeathCause d) {
        return d.getName() + (d.isActive() ? "" : "(D)") + "\n";
    }

    public String getPersistable() {
        String result = "";
        for (DeathCause cause : _deathCauses) {
            result += cause.getName() + "," + cause.isActive();
            if (cause != _deathCauses.get(_deathCauses.size() - 1)) {
                result += ";";
            }
        }
        return result;
    }

    public void fromPersistable(String persistable) {
        String[] keyvals = persistable.split(";");
        _deathCauses.clear();
        for (String entry : keyvals) {
            String[] keyval = entry.split(",");
            String name = keyval[0];
            boolean active = Parse.bool(keyval[1]);
            DeathCause cause = DeathCauses.get().get(name);
            cause.setActive(active);
            _deathCauses.add(cause);
        }
    }
}
