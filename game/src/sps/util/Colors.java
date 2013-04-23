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

    //Taken from: http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
    private static final float golden_ratio_conjugate = 0.618033988749895f;
    private static float hueBase = 0f;

    public static Color randomPleasant() {
        hueBase += golden_ratio_conjugate;
        hueBase %= 1f;
        return hsv(hueBase, 0.7f, 0.95f);
    }

    //Taken from: http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
    public static Color hsv(float hue, float saturation, float value) {
        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0:
                return rgbToColor(value, t, p);
            case 1:
                return rgbToColor(q, value, p);
            case 2:
                return rgbToColor(p, value, t);
            case 3:
                return rgbToColor(p, q, value);
            case 4:
                return rgbToColor(t, p, value);
            case 5:
                return rgbToColor(value, p, q);
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public static Color rgbToColor(float r, float g, float b) {
        return new Color(r, g, b, 1);
    }
}
