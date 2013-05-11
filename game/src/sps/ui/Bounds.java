package sps.ui;

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
}
