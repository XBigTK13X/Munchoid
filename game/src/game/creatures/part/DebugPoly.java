package game.creatures.part;

import game.GameConfig;
import sps.draw.Shapes;

import java.awt.*;

public class DebugPoly implements Design {
    int _dots;

    public DebugPoly(int dots) {
        _dots = GameConfig.DevDebugJointGridWithSquares ? 2 : dots;

    }

    @Override
    public int[][] create(int width, int height) {
        int rad = Math.min(width, height) / 2;
        int sides = _dots + 2;
        int rotDegs = -90 + 180 / -sides;
        Polygon p = Shapes.regular(sides, rad, rotDegs);
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