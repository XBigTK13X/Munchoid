package game;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
        }
        Sprite result = _lastRandom;
        while (_lastRandom == result) {
            result = RNG.pick(_backgrounds);
        }
        _lastRandom = result;
        return result;
    }

    public static void cacheScreenSize() {
        int scaleWidth = (int) (Screen.width(100) * GameConfig.OptGraphicsDetailScale);
        int scaleHeight = (int) (Screen.height(100) * GameConfig.OptGraphicsDetailScale);
        add(GameConfig.OptSimpleBackgrounds ?
                BackgroundMaker.radialDark(scaleWidth, scaleHeight) :
                BackgroundMaker.printedCircuitBoard(scaleWidth, scaleHeight));
    }
}
