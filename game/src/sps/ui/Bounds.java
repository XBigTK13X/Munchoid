package sps.ui;

import sps.entities.HitTest;

public class Bounds {
    public final int X;
    public final int Y;
    public final int X2;
    public final int Y2;
    public final int Width;
    public final int Height;


    public Bounds(int x, int y, int x2, int y2) {
        this((float) x, (float) y, x2 - x, y2 - y);
    }

    public Bounds(float x, float y, int width, int height) {
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
