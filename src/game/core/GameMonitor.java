package game.core;

import com.badlogic.gdx.Gdx;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;
import sps.core.Loader;
import sps.core.Logger;
import sps.display.Window;

import javax.swing.*;

public class GameMonitor extends Thread {
    private static int __millisecondsWait = 200;
    private static int _updateCount;

    public static void keepAlive() {
        _updateCount++;
    }

    private static GameMonitor _monitor;

    public static void monitor() {
        if (_monitor == null) {
            _monitor = new GameMonitor();
            _monitor.start();
        }
        else {
            Logger.error("Only one monitor can exist per game");
        }
    }

    private int _maxStalledMilliseconds;
    private int _stalledMilliseconds;
    private int _lastUpdateCount;

    private GameMonitor() {
        _stalledMilliseconds = _maxStalledMilliseconds = GameConfig.ThreadMaxStalledMilliseconds;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(__millisecondsWait);
                if (_updateCount == _lastUpdateCount) {
                    _stalledMilliseconds -= __millisecondsWait;
                    if (_stalledMilliseconds <= 0) {
                        Logger.error("The game appears to have hung for at least " + GameConfig.ThreadMaxStalledMilliseconds + " milliseconds. Forcing shutdown and recording the failure.");

                        String errorMessage = "Unfortunately, an error caused Munchoid to freeze.\nThis likely happened because the graphics settings were too high for this computer.\nYou might be able to play by changing the graphics from Pretty to Fast in the Options menu.";
                        Logger.error(errorMessage);
                        try {
                            FileUtils.writeStringToFile(Loader.get().userSave("Munchoid", "game.crash"), errorMessage);
                        }
                        catch (Exception e) {
                            Logger.exception(e, false);
                        }
                        System.exit(1);
                        return;
                    }
                }
                else {
                    _lastUpdateCount = _updateCount;
                    _stalledMilliseconds = _maxStalledMilliseconds;
                }
            }
            catch (Exception e) {
                Logger.exception(e, false);
            }
        }
    }
}
