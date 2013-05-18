package game.creatures.part;

import sps.core.Point2;
import sps.entities.HitTest;

public class RoundedRectangleBody implements Design {
    @Override
    public int[][] create(int width, int height) {
        int[][] result = Common.rectangle(width, height);
        float max = Math.max(width, height);
        float cornerRad2 = (float) Math.pow(max / 2, 2);

        Point2 center = new Point2(width / 2, height / 2);

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                float cenRad2 = HitTest.getDistanceSquare(ii, center.X, jj, center.Y);
                if (cenRad2 <= cornerRad2) {
                    result[ii][jj] = Design.Empty;
                }
            }
        }
        Common.invert(result);
        return result;
    }
}
