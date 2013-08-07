package sps.util;

import sps.entities.HitTest;

public class Bounds {
    public final int X;
    public final int Y;
    public final int X2;
    public final int Y2;
    public final int Width;
    public final int Height;


    public static Bounds fromPoints(int x, int y, int x2, int y2) {
        return new Bounds((float) x, (float) y, x2 - x, y2 - y);
    }

    public static Bounds fromDimensions(float x, float y, int width, int height) {
        return new Bounds(x, y, width, height);
    }

    public static Bounds fromPoints(float x, float y, float x2, float y2) {
        return fromPoints((int) x, (int) y, (int) x2, (int) y2);
    }

    private Bounds(float x, float y, int width, int height) {
        X = (int) x;
        Y = (int) y;
        Width = width;
        Height = height;
        X2 = X + width;
        Y2 = Y + height;
    }

    public boolean envelopes(Bounds b) {
        boolean left = HitTest.inBox(b.X, b.Y, this);
        boolean top = HitTest.inBox(b.X, b.Y2, this);
        boolean right = HitTest.inBox(b.X2, b.Y, this);
        boolean bottom = HitTest.inBox(b.X2, b.Y2, this);

        return left && right && top && bottom;
    }

    public String debug() {
        return "(" + X + "," + Y + ")>(" + X2 + "," + Y2 + ")";
    }
}
