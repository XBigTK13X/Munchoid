package game.core;

import com.badlogic.gdx.Gdx;
import sps.core.Logger;

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
                        Logger.error("The game appears to have hung for at least 10 seconds. Forcing it to close.");
                        Gdx.app.exit();
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
