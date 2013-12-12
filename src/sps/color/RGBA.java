package sps.color;

import com.badlogic.gdx.graphics.Color;
import sps.util.MathHelper;

public class RGBA implements ColorSpec<RGBA> {
    public static RGBA fromRGB(float r, float g, float b) {
        return new RGBA(r, g, b, 1f);
    }

    private static float __base = 255f;

    public float R;
    public float G;
    public float B;
    public float A;

    public RGBA(int r, int g, int b) {
        this(r, g, b, (int) __base);
    }

    public RGBA(int r, int g, int b, int a) {
        this(r / __base, g / __base, b / __base, a / __base);
    }

    public RGBA(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public RGBA(float r, float g, float b, float a) {
        this.R = r;
        this.G = g;
        this.B = b;
        this.A = a;
    }

    @Override
    public Color toColor() {
        return new Color(R, G, B, A);
    }

    @Override
    public ColorSpec average(RGBA target) {
        return lerp(50, target);
    }

    @Override
    public ColorSpec lerp(float startPercent, RGBA target) {
        float[] i = MathHelper.lerp(startPercent, R, target.R, G, target.G, B, target.B, A, target.A);
        return new RGBA(i[0], i[1], i[2], i[3]);
    }
}
