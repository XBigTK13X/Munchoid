package game.creatures.part;

import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;

import java.util.ArrayList;
import java.util.List;

public class StarFishBody implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = Common.rectangle(width, height);
        float cornerRad = (width + height) / 6;
        float cornerRad2 = (float) Math.pow(cornerRad, 2);
        List<Point2> corners = new ArrayList<Point2>();
        corners.add(new Point2(cornerRad, cornerRad));
        corners.add(new Point2(width - cornerRad, cornerRad));
        corners.add(new Point2(cornerRad, height - cornerRad));
        corners.add(new Point2(width - cornerRad, height - cornerRad));

        int fudgeFactor = (int) (width * .05f);
        //TODO Instead of 4 point and then inverse,
        // compare dist to 4 corners and then the center.
        // If further from center than corner, hide the point

        Point2 center = new Point2(width / 2, height / 2);

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                for (Point2 corner : corners) {
                    float rad2 = HitTest.getDistanceSquare(corner.X, ii, corner.Y, jj);
                    float cenRad2 = HitTest.getDistanceSquare(ii, center.X, jj, center.Y);
                    if (cenRad2 < rad2 && rad2 < cornerRad2 + RNG.next(-fudgeFactor, fudgeFactor)) {

                        result[ii][jj] = false;
                    }
                }
            }
        }
        Common.invert(result);
        return result;
    }
}
