package game.creatures.part;

import sps.core.RNG;
import sps.util.Shapes;

import java.awt.*;

public class PolyAccent implements Design {
    @Override
    public int[][] create(int width, int height) {
        int[][] result = new int[width][height];

        int radius = Math.min(width, height) / 3;

        Polygon detail = Shapes.regular(RNG.next(4, 9), radius, RNG.next(0, 360));
        detail.translate(width / 2, height / 2);
        for (int ii = 0; ii < result.length; ii++) {
            for (int jj = 0; jj < result[0].length; jj++) {
                if (detail.contains(ii, jj)) {
                    result[ii][jj] = Design.BaseColor;
                }
            }
        }

        return result;
    }
}
