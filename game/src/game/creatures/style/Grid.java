package game.creatures.style;

import game.creatures.BodyPart;
import game.creatures.PartFunction;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.Bounds;

public class Grid {
    //Each part is divided into a 3x3 grid.
    /*
        The default grid looks like

           789
           456
           123

        */

    private static final int[] gridRots = {-135, -90, -45, 180, 0, 0, 135, 90, 45};

    //Based on the attached joints, determine a part's location
    public static Point2 getOrigin(BodyPart part) {
        BodyPart parent = part.getParent();
        if (parent == null) {
            return new Point2(0, 0);
        }

        Point2 gridParXChildY = parent.getJoints().getGridConnectionTo(part);

        if (gridParXChildY.X == 5) {
            Point2 partCenterOffset = new Point2(-part.getWidth() / 2, -part.getHeight() / 2);
            Point2 parentCenterOffset = new Point2(parent.getWidth() / 2, parent.getHeight() / 2);
            return partCenterOffset.add(parentCenterOffset);
        }
        else {
            float rotRads = (float) ((gridRots[(int) gridParXChildY.X - 1]) * Math.PI / 180);
            int radius = Math.min(part.getWidth() / 2, part.getHeight() / 2);
            Point2 mult = new Point2((float) Math.cos(rotRads), (float) Math.sin(rotRads));
            Point2 parentGridDist = new Point2((parent.getWidth() / PartFunction.GridLocFraction) / 2, (parent.getHeight() / PartFunction.GridLocFraction) / 2);
            Point2 jointCenter = parent.getJoints().get((int) gridParXChildY.X).getLocalCenter();

            return jointCenter.add(radius * mult.X + parentGridDist.X * mult.X, radius * mult.Y + parentGridDist.Y * mult.Y);
        }
    }

    private static Bounds gridRange(Integer gridLoc, int width, int height) {
        //Convert a grid location (1->9) into coordinate ranges.
        // For example, location 1 corresponds to the range (0,0)->(33,33)
        // Using this calculation makes it easier to break the grid into smaller pieces
        // and increase control over part placement

        int j = gridLoc - 1;
        int n = PartFunction.GridLocFraction;
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
