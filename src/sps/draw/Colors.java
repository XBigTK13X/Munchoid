package sps.draw;

import com.badlogic.gdx.graphics.Color;
import sps.core.RNG;
import sps.util.MathHelper;

public class Colors {
    private static float base = 255f;

    public static Color rgb(int r, int g, int b) {
        return rgb(r, g, b, 255);
    }

    public static Color rgb(int r, int g, int b, int a) {
        return new Color(r / base, g / base, b / base, a / base);
    }

    private static int __shadePercent = 65;

    public static Color lighten(Color color) {
        return shade(color, __shadePercent);
    }

    public static Color darken(Color color) {
        return shade(color, -__shadePercent);
    }

    public static Color shade(Color color, int shadePercent) {
        if (shadePercent == 0) {
            return color;
        }

        float mult = Math.abs(MathHelper.clamp(shadePercent, -100, 100) / 100f);

        if (shadePercent > 0) {
            mult += 1f;
        }
        else {
            mult = 1f - mult;
        }

        return new Color(color.r * mult, color.g * mult, color.b * mult, color.a);
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

        return new HSV(__hueBase, 0.7f, 0.95f).toRGBColor();
    }

    public static Color hueShift(Color color, float shift) {
        HSV hsv = HSV.fromRGB(color);
        hsv.H += shift;
        while (hsv.H >= 1f) {
            hsv.H -= 1f / 6;
        }
        while (hsv.H < 0.0) {
            hsv.H += 1f / 6;
        }
        return hsv.toRGBColor();
    }

    public static Color fromRGB(float r, float g, float b) {
        return new Color(r, g, b, 1);
    }

    public static Color[] gradient(Color start, Color end, int steps) {
        Color[] gradient = new Color[steps];
        for (int ii = 0; ii < steps; ii++) {
            float sP = (float) ii / steps;
            float eP = 1 - sP;
            gradient[ii] = new Color(start.r * sP + end.r * eP, start.g * sP + end.g * eP, start.b * sP + end.b * eP, start.a * sP + end.a * eP);
        }
        return gradient;
    }

    public static int percentDifference(Color c1, Color c2) {
        return (int) (25 * (Math.abs(c1.a - c2.a) + Math.abs(c1.b - c2.b) + Math.abs(c1.r - c2.r) + Math.abs(c1.g - c2.g)));
    }

    public static Color divide(Color c, float constant) {
        Color result = new Color(c);
        result.r = c.r / constant;
        result.g = c.g / constant;
        result.b = c.b / constant;
        result.a = c.a / constant;
        return result;
    }

    public static Color blend(Color c1, Color c2) {
        float r = c1.r + c2.r / 2;
        float g = c1.g + c2.g / 2;
        float b = c1.b + c2.b / 2;
        return new Color(r, g, b, 1);
    }
}
