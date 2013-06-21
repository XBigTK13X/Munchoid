package sps.ui;

import game.states.Arena;
import sps.core.Logger;
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
        boolean left = HitTest.inBox(b.X, b.Y, Arena.getBounds());
        boolean top = HitTest.inBox(b.X, b.Y2, Arena.getBounds());
        boolean right = HitTest.inBox(b.X2, b.Y, Arena.getBounds());
        boolean bottom = HitTest.inBox(b.X2, b.Y2, Arena.getBounds());

        Logger.info("ME: " + debug() + ", b: " + b.debug());
        Logger.info("SIDES: " + left + "," + top + "," + right + "," + bottom);

        return HitTest.inBox(b.X, b.Y, Arena.getBounds()) && HitTest.inBox(b.X, b.Y2, Arena.getBounds()) && HitTest.inBox(b.X2, b.Y, Arena.getBounds()) && HitTest.inBox(b.X2, b.Y2, Arena.getBounds());
    }

    public String debug() {
        return "X: " + X + ",Y: " + Y + ",X2: " + X2 + ",Y2: " + Y2;
    }
}
