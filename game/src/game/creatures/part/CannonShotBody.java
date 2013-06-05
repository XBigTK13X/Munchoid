package game.creatures.part;

import sps.core.Point2;
import sps.entities.HitTest;

import java.util.ArrayList;
import java.util.List;

public class CannonShotBody implements Design {
    private static final List<Point2> __holes;

    static {
        List<Point2> holes = new ArrayList<Point2>();
        holes.add(new Point2(0, 0));
        holes.add(new Point2(0, 1));
        holes.add(new Point2(1, 0));
        holes.add(new Point2(1, 1));
        holes.add(new Point2(1 / 2f, 0));
        holes.add(new Point2(1 / 2f, 1));
        holes.add(new Point2(0, 1 / 2f));
        holes.add(new Point2(1, 1 / 2f));
        __holes = holes;
    }

    @Override
    public int[][] create(int width, int height) {
        int[][] result = Common.rectangle(width, height);
        result = Common.empty(result);
        float holeRad = Math.min(width, height) / 3;


        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                for (Point2 hole : __holes) {
                    float rad = HitTest.getDistance(hole.X * width, hole.Y * height, ii, jj);
                    if (rad < holeRad) {
                        result[ii][jj] = Design.BaseColor;
                    }
                }
            }
        }

        Common.invert(result);
        return result;
    }
}
