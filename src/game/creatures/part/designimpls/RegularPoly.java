package game.creatures.part.designimpls;

import game.creatures.part.Common;
import game.creatures.part.Design;
import sps.core.RNG;
import sps.draw.Shapes;

import java.awt.*;

public class RegularPoly implements Design {
    @Override
    public int[][] create(int width, int height) {
        int rad = Math.min(width, height) / 2;
        int sides = RNG.next(3, 8);
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
