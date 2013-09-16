package game.save;

import sps.core.Loader;
import sps.states.StateManager;

import java.io.File;

public class Persistence {
    private static Persistence __instance;

    private static final File __autoSave = Loader.get().save("autosave.dat");

    public static Persistence get() {
        if (__instance == null) {
            __instance = new Persistence();
        }
        return __instance;
    }

    private Persistence() {

    }

    public void configSave() {

    }

    public void configLoad() {

    }

    public void autoSave() {
        Serialize.toFile(StateManager.get().takeSnapshot(), __autoSave);
    }

    public GameSnapshot autoLoad() throws RuntimeException {
        if (saveFileExists()) {
            try {
                GameSnapshot snapshot = Serialize.fromFile(__autoSave, GameSnapshot.class);
                if (snapshot.RecordedVersion != GameSnapshot.Version) {
                    throw new RuntimeException("Save game version mismatch. Recorded version is " + snapshot.RecordedVersion + " but the current version is " + GameSnapshot.Version);
                }
                return snapshot;
            }
            catch (Exception e) {
                throw new RuntimeException("Save game file out of date.");
            }
        }
        else {
            throw new RuntimeException("Save file does not exist. Checked for: " + __autoSave.getAbsolutePath());
        }
    }

    public boolean saveFileExists() {
        return __autoSave.exists();
    }
}
