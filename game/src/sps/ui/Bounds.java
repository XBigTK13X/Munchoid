package sps.ui;

public class Bounds {
    public final int X;
    public final int Y;
    public final int Width;
    public final int Height;

    public Bounds(float x, float y, int width, int height) {
        X = (int) x;
        Y = (int) y;
        Width = width;
        Height = height;
    }
}
