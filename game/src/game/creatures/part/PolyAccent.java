package game.creatures.part;

import sps.core.RNG;
import sps.draw.Shapes;

import java.awt.*;

public class PolyAccent implements Design {
    @Override
    public int[][] create(int width, int height) {
        int radius = Math.min(width, height) / 3;
        return Forms.polygon(RNG.next(4, 9), radius, RNG.next(0, 360), width, height);
    }
}
