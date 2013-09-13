package game;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.states.StateManager;

import java.io.IOException;

public class Persistence {
    private static Persistence __instance;

    public static Persistence get() {
        if (__instance == null) {
            __instance = new Persistence();
        }
        return __instance;
    }

    private Gson _serializer;

    private Persistence() {
        _serializer = new Gson();
    }

    public void save() {
        String json = _serializer.toJson(StateManager.get());
        try {
            FileUtils.writeStringToFile(Loader.get().data("save.nnue"), json);
        }
        catch (IOException e) {
            Logger.exception(e);
        }
    }

    public void load() {
        if (saveFileExists()) {

            try {
                String json = FileUtils.readFileToString(Loader.get().data("save.nnue"));
                _serializer.fromJson(json, StateManager.class);
            }
            catch (IOException e) {
                Logger.exception(e);
            }

        }
    }

    public boolean saveFileExists() {
        return Loader.get().data("save.nnue").exists();
    }
}
