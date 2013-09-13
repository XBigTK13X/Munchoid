package game.population;

import game.GameConfig;
import sps.core.Logger;
import sps.display.Screen;
import sps.text.Text;
import sps.text.TextPool;

import java.util.List;

public class DiseaseMonitor {
    private Text _display;
    private List<Disease> _diseases;
    private String _header;
    private boolean _top;

    private int _disableIndex = 0;

    public DiseaseMonitor(boolean top) {
        _diseases = Diseases.get().getRandom(top, GameConfig.NumberOfTournaments);
        _header = top ? "TOP" : "BOTTOM";
        _top = top;
        generateDisplay();
    }

    public void generateDisplay() {
        _display = TextPool.get().write("", _top ? Screen.pos(5, 95) : Screen.pos(75, 95));
    }

    public void disableOne() {
        _diseases.get(_disableIndex++).setActive(false);
    }

    public int totalDeaths(Population population) {
        int totalDeaths = 0;
        for (Disease d : _diseases) {
            if (d.isActive()) {
                totalDeaths += population.deathsCausedBy(d);
                if (GameConfig.DevDebugPopulationGrowth) {
                    Logger.info(d.getName() + " caused " + population.deathsCausedBy(d) + " deaths");
                }
            }
        }
        return totalDeaths;
    }

    public void update() {
        String tD = _header + "\n";
        for (Disease d : _diseases) {
            tD += diseaseDisplay(d);
        }
        _display.setMessage(tD);
        _display.setScale(.5f);
    }

    private String diseaseDisplay(Disease d) {
        return d.getName() + (d.isActive() ? "" : "(D)") + "\n";
    }
}
