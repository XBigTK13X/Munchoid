package game.save;
import sps.core.Loader;
import sps.states.StateManager;

import java.io.File;

public class Persistence {
    private static Persistence __instance;

    private static final File __autoSave = Loader.get().data("nnue.save");

    public static Persistence get() {
        if (__instance == null) {
            __instance = new Persistence();
        }
        return __instance;
    }

    private Persistence() {

    }

    public void save() {
        Serialize.toFile(StateManager.get().takeSnapshot(), __autoSave);
    }

    public void load() {
        if (saveFileExists()) {
            GameSnapshot snapshot = Serialize.fromFile(__autoSave, GameSnapshot.class);
            StateManager.get().loadFrom(snapshot);

        }
    }

    public boolean saveFileExists() {
        return __autoSave.exists();
    }
}
