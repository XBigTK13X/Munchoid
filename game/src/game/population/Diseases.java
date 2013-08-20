package game.population;

import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.util.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Diseases {
    private static Diseases __instance;

    public static Diseases get() {
        if (__instance == null) {
            __instance = new Diseases();
        }
        return __instance;
    }

    private List<Disease> _diseases;
    private List<Disease> _top;
    private List<Disease> _bottom;

    private Diseases() {
        _diseases = new ArrayList<Disease>();
        try {
            for (String line : FileUtils.readLines(Loader.get().data("diseases.csv"))) {
                if (!line.contains("##") && line.length() > 1) {
                    String[] values = line.split(",");
                    String name = values[0];
                    float mortalityRate = Parse.floa(values[1]);
                    float deathsPer100000 = Parse.floa(values[2]);
                    _diseases.add(new Disease(name, mortalityRate, deathsPer100000));
                }
            }
            Collections.sort(_diseases);

            _top = new ArrayList<Disease>();
            _bottom = new ArrayList<Disease>();
            for (int ii = 0; ii < _diseases.size(); ii++) {
                if (ii <= _diseases.size() / 2) {
                    _top.add(_diseases.get(ii));
                }
                else {
                    _bottom.add(_diseases.get(ii));
                }
            }

            Collections.shuffle(_top);
            Collections.shuffle(_bottom);
        }
        catch (IOException e) {
            Logger.exception(e);
        }
    }

    public List<Disease> getRandom(boolean top, int diseaseCount) {
        if (diseaseCount > _diseases.size() / 2) {
            throw new RuntimeException("There are more matches than available diseases. It is impossible to clear them all");
        }
        List<Disease> result = new ArrayList<Disease>();
        for (int ii = 0; ii < diseaseCount; ii++) {
            result.add(top ? _top.get(ii) : _bottom.get(ii));
        }
        return result;
    }
}
