package game.save;

import game.core.UserFiles;
import game.population.PopulationOverview;
import org.apache.commons.io.FileUtils;
import sps.core.Logger;
import sps.states.StateManager;
import sps.util.Scrambler;

import java.io.File;

public class Persistence {
    private static Persistence __instance;

    private static final File __autoSave = UserFiles.save();

    public static Persistence get() {
        if (__instance == null) {
            __instance = new Persistence();
        }
        return __instance;
    }

    private Persistence() {

    }

    private Thread _worker;
    private boolean _saveFileIsBad = false;

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
                        String scrambled = Scrambler.scramble(overview.takeSnapshot().toPersistable());
                        FileUtils.writeStringToFile(__autoSave, scrambled);
                        _saveFileIsBad = false;
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
                String plainText = Scrambler.descramble(FileUtils.readFileToString(__autoSave));
                GameSnapshot snapshot = GameSnapshot.fromPersistable(plainText);
                if (snapshot.SaveFormatVersion != GameSnapshot.CurrentSaveFormatVersion) {
                    throw new RuntimeException("Save game version mismatch. Recorded version is " + snapshot.SaveFormatVersion + " but the current version is " + GameSnapshot.CurrentSaveFormatVersion);
                }
                _saveFileIsBad = false;
                return snapshot;
            }
            catch (Exception e) {
                _saveFileIsBad = true;
                Logger.exception(e, false);
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

    public boolean isSaveBad() {
        return _saveFileIsBad;
    }
}
