package game.population;

import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.util.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeathCauses {
    private static DeathCauses __instance;

    public static DeathCauses get() {
        if (__instance == null) {
            __instance = new DeathCauses();
        }
        return __instance;
    }

    private List<DeathCause> _deathCauses;
    private List<DeathCause> _top;
    private List<DeathCause> _bottom;

    private DeathCauses() {
        _deathCauses = new ArrayList<DeathCause>();
        try {
            for (String line : FileUtils.readLines(Loader.get().data("causes_of_death.csv"))) {
                if (!line.contains("##") && line.length() > 1) {
                    String[] values = line.split(",");
                    String name = values[0];
                    float deathsPer100000 = Parse.floa(values[2]);
                    _deathCauses.add(new DeathCause(name, deathsPer100000));
                }
            }
            Collections.sort(_deathCauses);

            _top = new ArrayList<>();
            _bottom = new ArrayList<>();
            for (int ii = 0; ii < _deathCauses.size(); ii++) {
                if (ii <= _deathCauses.size() / 2) {
                    _top.add(_deathCauses.get(ii));
                }
                else {
                    _bottom.add(_deathCauses.get(ii));
                }
            }
        }
        catch (IOException e) {
            Logger.exception(e);
        }
    }

    public List<DeathCause> getRandom(boolean top, int causeCount) {
        if (causeCount > _deathCauses.size() / 2) {
            throw new RuntimeException("There are more tournaments than available causes of death. It is impossible to clear them all");
        }
        Collections.shuffle(_top);
        Collections.shuffle(_bottom);
        List<DeathCause> result = new ArrayList<>();
        for (int ii = 0; ii < causeCount; ii++) {
            result.add(top ? _top.get(ii) : _bottom.get(ii));
            result.get(result.size() - 1).setActive(true);
        }
        return result;
    }
}
