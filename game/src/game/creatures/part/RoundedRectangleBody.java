package game.creatures.part;

import sps.core.Point2;
import sps.entities.HitTest;

public class RoundedRectangleBody implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = Common.rectangle(width, height);
        float max = Math.max(width, height);
        float cornerRad2 = (float) Math.pow(max / 2, 2);

        //TODO Instead of 4 point and then inverse,
        // compare dist to 4 corners and then the center.
        // If further from center than corner, hide the point

        Point2 center = new Point2(width / 2, height / 2);

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                float cenRad2 = HitTest.getDistanceSquare(ii, center.X, jj, center.Y);
                if (cenRad2 <= cornerRad2) {
                    result[ii][jj] = false;
                }
            }
        }
        Common.invert(result);
        return result;
    }
}
