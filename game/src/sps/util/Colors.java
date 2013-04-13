package sps.util;

import com.badlogic.gdx.graphics.Color;
import sps.core.RNG;

public class Colors {
    private static float base = 255f;

    public static Color rgb(int r, int g, int b) {
        return new Color(r / base, g / base, b / base, 1f);
    }

    public static Color random() {
        return rgb(RNG.next(0, 255, false), RNG.next(0, 255, false), RNG.next(0, 255, false));
    }
}
