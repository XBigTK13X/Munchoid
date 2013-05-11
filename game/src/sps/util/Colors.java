package sps.util;

import com.badlogic.gdx.graphics.Color;
import sps.core.RNG;

public class Colors {
    private static class HSV {
        public float H;
        public float S;
        public float V;

        public HSV() {
            this(0f, 0f, 0f);
        }

        public HSV(float hue, float saturation, float value) {
            H = hue;
            S = saturation;
            V = value;
        }

        public Color toColor() {
            return hsv(H, S, V);
        }
    }

    private static float base = 255f;

    public static Color rgb(int r, int g, int b) {
        return new Color(r / base, g / base, b / base, 1f);
    }

    private static float __shadePercent = .65f;

    public static Color lighten(Color color) {
        HSV hsv = fromRGB(color);
        hsv.S = hsv.S * __shadePercent;
        return hsv.toColor();
    }

    public static Color darken(Color color) {
        HSV hsv = fromRGB(color);
        hsv.V = hsv.V * __shadePercent;
        return hsv.toColor();
    }

    public static Color shade(Color color, int breadth) {
        HSV hsv = fromRGB(color);
        hsv.V = hsv.V * ((100 + breadth) / 100f);
        if (hsv.V > 1) {
            hsv.V = 1;
        }
        if (hsv.V < 0) {
            hsv.V = 0;
        }
        return hsv.toColor();
    }

    public static Color random() {
        return rgb(RNG.next(0, 255, false), RNG.next(0, 255, false), RNG.next(0, 255, false));
    }

    //From: http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
    private static final float __goldenRatioCongujate = (float) Math.abs(1 - Math.sqrt(5)) / 2; //approx 0.618033988749895f;
    private static float __hueBase;
    private static boolean __hueBaseRandomized = false;

    public static Color randomPleasant() {
        if (!__hueBaseRandomized) {
            __hueBase = RNG.next(0, 360) / 360f;
            __hueBaseRandomized = true;
        }
        __hueBase += __goldenRatioCongujate;
        __hueBase %= 1f;

        return hsv(__hueBase, 0.7f, 0.95f);
    }

    //From: http://stackoverflow.com/questions/1664140/js-function-to-calculate-complementary-colour
    public static HSV fromRGB(Color rgb) {
        HSV hsv = new HSV();
        float max = Math.max(Math.max(rgb.r, rgb.g), rgb.b);
        float dif = max - Math.min(Math.min(rgb.r, rgb.g), rgb.b);
        hsv.S = (max == 0.0) ? 0 : (dif / max);
        if (hsv.S == 0) {
            hsv.H = 0;
        }
        else if (rgb.r == max) {
            hsv.H = 1f / 6f * (rgb.g - rgb.b) / dif;
        }
        else if (rgb.g == max) {
            hsv.H = 2f / 6f + 1f / 6f * (rgb.b - rgb.r) / dif;
        }
        else if (rgb.b == max) {
            hsv.H = 4f / 6f + 1f / 6f * (rgb.r - rgb.g) / dif;
        }
        if (hsv.H < 0.0) {
            hsv.H += 1f / 6f;
        }
        hsv.V = max;

        return hsv;
    }

    public static Color hueShift(Color color, float shift) {
        HSV hsv = fromRGB(color);
        hsv.H += shift;
        while (hsv.H >= 1f) {
            hsv.H -= 1f / 6;
        }
        while (hsv.H < 0.0) {
            hsv.H += 1f / 6;
        }
        return hsv.toColor();
    }

    //From: http://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe
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
                throw new RuntimeException("HSV to RGB failure. Input was " + hue + ", " + saturation + ", " + value + ". Calculated H: " + h);
        }
    }

    public static Color rgbToColor(float r, float g, float b) {
        return new Color(r, g, b, 1);
    }

    public static Color hsvAverage(Color a, Color b) {
        HSV ha = fromRGB(a);
        HSV hb = fromRGB(b);
        return hsv((ha.H + hb.H) / 2, (ha.S + hb.S) / 2, (ha.V + hb.V) / 2);
    }

    public static Color[][] genArr(int width, int height, Color color) {
        Color[][] base = new Color[width][height];
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = color;
            }
        }
        return base;
    }

    private static final int perlinOctaves = 6;

    public static Color[][] getPerlinGrid(int width, int height, Color start, Color end) {
        float[][] noise = Noise.perlin(width, height, RNG.next(perlinOctaves - 1, perlinOctaves + 1));
        return Noise.mapGradient(start, end, noise);
    }
}
