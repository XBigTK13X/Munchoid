package sps.color;

import sps.util.MathHelper;

//Bitwise color operations taken from: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/graphics/Color.java
public class Color {
    protected static final float COLOR_DEPTH = 255f; ///32 - 8 bits per channel (RGBA)

    public static final Color CLEAR = new Color(0, 0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color BLACK = new Color(0, 0, 0, 1);
    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color GREEN = new Color(0, 1, 0, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color LIGHT_GRAY = new Color(0.75f, 0.75f, 0.75f, 1);
    public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1);
    public static final Color DARK_GRAY = new Color(0.25f, 0.25f, 0.25f, 1);
    public static final Color PINK = new Color(1, 0.68f, 0.68f, 1);
    public static final Color ORANGE = new Color(1, 0.78f, 0, 1);
    public static final Color YELLOW = new Color(1, 1, 0, 1);
    public static final Color MAGENTA = new Color(1, 0, 1, 1);
    public static final Color CYAN = new Color(0, 1, 1, 1);

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    protected final com.badlogic.gdx.graphics.Color _gdxColor;

    public Color(float r, float g, float b, float a) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = clamp(a);
        _gdxColor = new com.badlogic.gdx.graphics.Color(this.r, this.g, this.b, this.a);
    }

    public Color(int rgba8888) {
        this(((rgba8888 & 0xff000000) >>> 24) / 255f, ((rgba8888 & 0x00ff0000) >>> 16) / 255f, ((rgba8888 & 0x0000ff00) >>> 8) / 255f, ((rgba8888 & 0x000000ff)) / 255f);
    }

    public Color(Color color) {
        this(color.r, color.g, color.b, color.a);
    }

    public Color mul(Color target) {
        return new Color(r * target.r, g * target.g, b * target.b, a * target.a);
    }

    public Color newRed(float red) {
        return new Color(red, g, b, a);
    }

    public Color newGreen(float green) {
        return new Color(r, green, b, a);
    }

    public Color newBlue(float blue) {
        return new Color(r, g, blue, a);
    }

    public Color newAlpha(float alpha) {
        return new Color(r, g, b, alpha);
    }

    protected float clamp(float value) {
        return MathHelper.clamp(value, 0.0f, 1.0f);
    }

    public com.badlogic.gdx.graphics.Color getGdxColor() {
        return _gdxColor;
    }

    public int rgb888() {
        return ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255);
    }

    public int rgba8888() {
        return ((int) (r * 255) << 24) | ((int) (g * 255) << 16) | ((int) (b * 255) << 8) | (int) (a * 255);
    }
}
