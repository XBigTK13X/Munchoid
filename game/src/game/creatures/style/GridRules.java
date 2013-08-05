package game.creatures.style;

import game.creatures.BodyPart;
import game.creatures.PartFunction;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.Bounds;

public class GridRules {
    //Each part is divided into a 3x3 grid.
    //Children are placed randomly within any point
    //that lies within that part function's possible grid locs
    public static Point2 getOrigin(BodyPart part) {
        BodyPart parent = part.getParent();
        if (parent == null) {
            return new Point2(0, 0);
        }

        Point2 gridParXChildY = parent.getJoints().getGridConnectionTo(part);
        Point2 parPos = GridRules.randomPointInside((int) gridParXChildY.X, parent.getWidth(), parent.getHeight());
        Point2 offset = GridRules.randomPointInside((int) gridParXChildY.Y, part.getWidth(), part.getHeight());
        //TODO Better centering
        //offset.setX(-offset.X);
        //offset.setY(-offset.Y);

        //part.setRotation(part.getFunction().RotationOffset);

        return parPos.add(offset);

    }

    private static Bounds gridRange(Integer gridLoc, int width, int height) {
        //Convert a grid location (1->9) into coordinate ranges.
        // For example, location 1 corresponds to the range (0,0)->(33,33)
        // Using this calculation makes it easier to break the grid into smaller pieces
        // and increase control over part placement

        //Subtracting here inverts the Y axis
        int j = gridLoc - 1;
        int n = (int) Math.sqrt(PartFunction.GridSize);
        float m = 100 / (float) (n);
        int xMin = (int) (m * (j % n));
        int yMin = (int) (m * Math.floor(j / n));
        int xMax = (int) (m * (1 + (j % n)));
        int yMax = (int) (m * (1 + (j / n)));
        return Bounds.fromPoints(xMin, yMin, xMax, yMax);
    }

    public static Point2 randomPointInside(Integer gridLoc, int width, int height) {
        Bounds b = gridRange(gridLoc, width, height);
        return pointInside(width, height, b.X, b.X2, b.Y, b.Y2);
    }

    public static Point2 centerOf(Integer gridLoc, BodyPart part) {
        int width = part.getWidth();
        int height = part.getHeight();
        Bounds b = gridRange(gridLoc, width, height);
        int xMid = (b.X + b.X2) / 2;
        int yMid = (b.Y + b.Y2) / 2;
        return pointInside(width, height, xMid, xMid, yMid, yMid);
    }

    private static Point2 pointInside(int width, int height, int minXPercent, int maxXPercent, int minYPercent, int maxYPercent) {
        return RNG.point(p(width, minXPercent), p(width, maxXPercent), p(height, minYPercent), p(height, maxYPercent));
    }

    private static int p(int dimension, int percent) {
        return (int) (dimension * (percent / 100f));
    }
}
