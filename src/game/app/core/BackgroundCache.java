package game.app.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.config.GameConfig;
import sps.core.Logger;
import sps.core.RNG;
import sps.display.Screen;

import java.util.ArrayList;
import java.util.List;

public class BackgroundCache {
    private static List<Sprite> _backgrounds = new ArrayList<>();

    public static void add(Sprite sprite) {
        _backgrounds.add(sprite);
    }

    public static void clear() {
        _backgrounds.clear();
    }

    private static Sprite _lastRandom;

    public static Sprite getRandom() {
        if (_backgrounds.size() <= 1) {
            Logger.error("No backgrounds were cached. Caching two random backgrounds. This should only happen while debugging.");
            cacheScreenSize();
            cacheScreenSize();
        }
        Sprite result = _lastRandom;
        while (_lastRandom == result) {
            result = RNG.pick(_backgrounds);
        }
        result.setSize(Screen.width(100), Screen.height(100));
        result.setPosition(0, 0);
        _lastRandom = result;
        return result;
    }

    public static Sprite cacheScreenSize() {
        int scaleWidth = (int) (Screen.width(100) * GameConfig.OptGraphicsDetailScale);
        int scaleHeight = (int) (Screen.height(100) * GameConfig.OptGraphicsDetailScale);
        Sprite result = GameConfig.OptSimpleBackgrounds ?
                BackgroundMaker.radialDark(scaleWidth, scaleHeight) :
                BackgroundMaker.printedCircuitBoard(scaleWidth, scaleHeight);
        add(result);
        return result;
    }

    public static Sprite createMenuBackground() {
        int scaleWidthPercent = (int) ((100f) * GameConfig.OptGraphicsDetailScale);
        int scaleHeightPercent = (int) ((100f) * GameConfig.OptGraphicsDetailScale);
        Sprite result = BackgroundMaker.noisyRadialDark(scaleWidthPercent, scaleHeightPercent);
        result.setPosition(0, 0);
        result.setSize(Screen.width(100), Screen.height(100));
        return result;
    }
}
