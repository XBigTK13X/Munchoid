package game.creatures.part;

import sps.draw.Shapes;

import java.awt.*;

public class DebugPoly implements Design {
    int _dots;

    public DebugPoly(int dots) {
        _dots = dots;
    }

    @Override
    public int[][] create(int width, int height) {
        int rad = Math.min(width, height) / 2;
        Polygon p = Shapes.regular(_dots + 2, rad, 0);
        p.translate(rad, rad);
        int[][] result = Common.rectangle(width, height);
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                if (p.contains(ii, jj)) {
                    result[ii][jj] = Design.BaseColor;
                }
                else {
                    result[ii][jj] = Design.Empty;
                }
            }
        }
        return result;
    }
}