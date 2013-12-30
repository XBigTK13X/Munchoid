package game.creatures.part;

import game.creatures.PartFunction;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SilhouetteSpecs {
    private File _source;
    private Map<File, List<PartFunction>> _entries;

    public SilhouetteSpecs(File source) throws IOException {
        _entries = new HashMap<>();
        for (String line : FileUtils.readLines(source)) {
            String[] raw = line.split("=");

            String imageName = raw[0];
            File image = new File(source.getParentFile(), imageName);

            _entries.put(image, new LinkedList<PartFunction>());

            String[] rawFunctions = raw[1].split(",");
            for (String function : rawFunctions) {
                _entries.get(image).add(PartFunction.lookup(function.trim()));
            }
        }
    }

    public Map<File, List<PartFunction>> getEntries() {
        return _entries;
    }
}
