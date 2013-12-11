package sps.color;

import com.badlogic.gdx.graphics.Color;

public class RGBA implements ColorSpec<RGBA> {
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
        return new RGBA((R + target.R) / 2f, (G + target.G) / 2f, (B + target.B) / 2f, (A + target.A) / 2f);
    }
}
