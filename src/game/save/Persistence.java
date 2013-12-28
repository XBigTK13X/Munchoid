package game.save;

import game.population.PopulationOverview;
import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.states.StateManager;

import java.io.File;

public class Persistence {
    private static Persistence __instance;

    private static final File __autoSave = Loader.get().userSave("Munchoid", "autosave.dat");

    public static Persistence get() {
        if (__instance == null) {
            __instance = new Persistence();
        }
        return __instance;
    }

    private Persistence() {

    }

    private Thread _worker;

    public boolean isBusy() {
        return _worker != null && _worker.isAlive();
    }

    public void autoSave() {
        if (!isBusy()) {
            _worker = new Thread() {
                @Override
                public void run() {
                    try {
                        PopulationOverview overview = (PopulationOverview) StateManager.get().current();
                        FileUtils.writeStringToFile(__autoSave, overview.takeSnapshot().toPersistable());
                    }
                    catch (Exception e) {
                        Logger.exception(e);
                    }
                }
            };
            _worker.start();
        }
    }

    public GameSnapshot autoLoad() {
        if (saveFileExists() && !isBusy()) {
            try {
                GameSnapshot snapshot = GameSnapshot.fromPersistable(FileUtils.readFileToString(__autoSave));
                if (snapshot.RecordedVersion != GameSnapshot.CurrentVersion) {
                    throw new RuntimeException("Save game version mismatch. Recorded version is " + snapshot.RecordedVersion + " but the current version is " + GameSnapshot.CurrentVersion);
                }
                return snapshot;
            }
            catch (Exception e) {
                Logger.exception(e);
                return null;
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
