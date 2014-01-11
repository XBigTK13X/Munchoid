package game.stages.common.creatures.part;

import sps.core.Point2;
import sps.draw.Shapes;
import sps.entities.HitTest;

import java.awt.*;

public class Forms {
    public static int[][] polygon(int sides, float radius, int rotDegrees, int resWidth, int resHeight) {
        int[][] result = new int[resWidth][resHeight];
        Polygon detail = Shapes.regular(sides, radius, rotDegrees);
        detail.translate(resWidth / 2, resHeight / 2);
        for (int ii = 0; ii < result.length; ii++) {
            for (int jj = 0; jj < result[0].length; jj++) {
                if (detail.contains(ii, jj)) {
                    result[ii][jj] = Design.BaseColor;
                }
            }
        }
        return result;
    }

    public static int[][] circle(float radius, int resWidth, int resHeight) {
        int[][] result = new int[resWidth][resHeight];
        Point2 center = new Point2(resWidth / 2, resHeight / 2);
        for (int ii = 0; ii < resWidth; ii++) {
            for (int jj = 0; jj < resHeight; jj++) {
                if (HitTest.getDistance(ii, jj, center.X, center.Y) <= radius) {
                    result[ii][jj] = Design.BaseColor;
                }
            }
        }
        return result;
    }
}
