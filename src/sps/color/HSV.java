package sps.color;

import com.badlogic.gdx.graphics.Color;
import sps.util.MathHelper;

public class HSV implements ColorSpec<HSV> {
    //Range: 0f -> 6f
    public float H;
    //Range: 0f -> 1f
    public float S;
    //Range: 0f -> 1f
    public float V;

    public HSV() {
        this(0f, 0f, 0f);
    }

    public HSV(float hue, float saturation, float value) {
        H = hue;
        S = saturation;
        V = value;
        clamp();
    }

    private void clamp() {
        H = MathHelper.clamp(H, 0f, 6f);
        S = MathHelper.clamp(S, 0f, 1f);
        V = MathHelper.clamp(V, 0f, 1f);
    }

    //From: http://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe
    public Color toColor() {
        clamp();
        int h = (int) (H * 6);
        float f = H * 6 - h;
        float p = V * (1 - S);
        float q = V * (1 - f * S);
        float t = V * (1 - (1 - f) * S);

        switch (h) {
            case 0:
                return new RGBA(V, t, p).toColor();
            case 1:
                return new RGBA(q, V, p).toColor();
            case 2:
                return new RGBA(p, V, t).toColor();
            case 3:
                return new RGBA(p, q, V).toColor();
            case 4:
                return new RGBA(t, p, V).toColor();
            case 5:
                return new RGBA(V, p, q).toColor();
            default:
                throw new RuntimeException("HSV to RGB failure. Input was " + H + ", " + S + ", " + V + ". Calculated H: " + h);
        }
    }

    public ColorSpec average(HSV target) {
        clamp();
        target.clamp();
        return interpolate(50, target);
    }

    @Override
    public ColorSpec interpolate(float startPercent, HSV target) {
        float[] i = MathHelper.interpolate(startPercent, H, target.H, S, target.S, V, target.V);
        return new HSV(i[0], i[1], i[2]);
    }

    public static HSV fromColor(Color color) {
        return fromRGB(color.r, color.g, color.b);
    }

    //From: http://stackoverflow.com/questions/1664140/js-function-to-calculate-complementary-colour
    public static HSV fromRGB(float r, float g, float b) {
        HSV hsv = new HSV();
        float max = Math.max(Math.max(r, g), b);
        float dif = max - Math.min(Math.min(r, g), b);
        hsv.S = (max == 0.0) ? 0 : (dif / max);
        if (hsv.S == 0) {
            hsv.H = 0;
        }
        else if (r == max) {
            hsv.H = 1f / 6f * (g - b) / dif;
        }
        else if (g == max) {
            hsv.H = 2f / 6f + 1f / 6f * (b - r) / dif;
        }
        else if (b == max) {
            hsv.H = 4f / 6f + 1f / 6f * (r - g) / dif;
        }
        if (hsv.H < 0.0) {
            hsv.H += 1f / 6f;
        }
        hsv.V = max;

        hsv.clamp();
        return hsv;
    }
}

