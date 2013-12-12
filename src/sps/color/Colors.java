package sps.color;

import com.badlogic.gdx.graphics.Color;
import sps.core.RNG;
import sps.util.MathHelper;

public class Colors {

    private static int __defaultBrightnessShiftPercent = 15;

    public static Color lighten(Color color) {
        return brightnessShift(color, __defaultBrightnessShiftPercent);
    }

    public static Color darken(Color color) {
        return brightnessShift(color, -__defaultBrightnessShiftPercent);
    }

    public static Color brightnessShift(Color color, int shiftPercent) {
        if (shiftPercent == 0) {
            return color;
        }
        HSV hsv = HSV.fromRGB(color.r, color.g, color.b);
        float shift = hsv.V * MathHelper.percentDecimal(shiftPercent);
        hsv.V += shift;

        return hsv.toColor();
    }

    public static Color random() {
        return new RGBA(RNG.next(0, 255, false), RNG.next(0, 255, false), RNG.next(0, 255, false)).toColor();
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

        return new HSV(__hueBase, 0.7f, 0.95f).toColor();
    }

    public static Color hueShift(Color color, float shift) {
        HSV hsv = HSV.fromColor(color);
        hsv.H += shift;
        while (hsv.H >= 1f) {
            hsv.H -= 1f / 6;
        }
        while (hsv.H < 0.0) {
            hsv.H += 1f / 6;
        }
        return hsv.toColor();
    }

    public static Color[] gradient(Color start, Color end, int steps) {
        Color[] gradient = new Color[steps];
        for (int ii = 0; ii < steps; ii++) {
            float sP = (float) ii / steps;
            gradient[ii] = interpolate(sP, start, end);
        }
        return gradient;
    }

    public static Color interpolate(float startPercent, Color start, Color end) {
        return new HSV(start.r, start.g, start.b).interpolate(startPercent, new HSV(end.r, end.g, end.b)).toColor();
    }
}
