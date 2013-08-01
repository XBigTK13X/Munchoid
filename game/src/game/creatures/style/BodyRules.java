package game.creatures.style;

import game.GameConfig;
import game.creatures.BodyPart;
import game.creatures.PartFunction;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.RNG;

public class BodyRules {
    //Each part is divided into a 3x3 grid.
    //Children are placed randomly within any point
    //that lies within that part function's possible grid locs
    public static Point2 getOrigin(BodyPart part) {
        BodyPart parent = part.getParent();
        if (parent == null) {
            return new Point2(0, 0);
        }

        Point2 gridParXChildY = parent.getConnections().getGridConnectionTo(part);
        Point2 parPos = BodyRules.gridRange((int) gridParXChildY.X, parent.getWidth(), parent.getHeight());
        Point2 offset = BodyRules.gridRange((int) gridParXChildY.Y, part.getWidth(), part.getHeight());
        offset.setX(-offset.X);
        offset.setY(-offset.Y);

        Logger.info("Part: " + part.getFunction() + "," + gridParXChildY);

        part.setRotation(part.getFunction().RotationOffset);

        return parPos.add(offset);

    }

    public static Point2 gridRange(Integer gridLoc, int width, int height) {
        //Convert a grid location (1->9) into coordinate ranges.
        // For example, location 1 corresponds to the range (0,0)->(33,33)
        // Using this calculation makes it easier to break the grid into smaller pieces
        // and increase control over part placement

        //Subtracting here inverts the Y axis
        int j = gridLoc - 1;
        int n = (int) Math.sqrt(PartFunction.GridSize);
        float m = 100 / (float) (n);
        int yMin = (int) (m * (j % n));
        int xMin = (int) (m * Math.floor(j / n));
        int yMax = (int) (m * (1 + (j % n)));
        int xMax = (int) (m * (1 + (j / n)));
        return pointInside(width, height, xMin, xMax, yMin, yMax);
    }

    private static Point2 pointInside(int width, int height, int minXPercent, int maxXPercent, int minYPercent, int maxYPercent) {
        if (GameConfig.DevPlaceInGridCenter) {
            return new Point2(p(width, (maxXPercent + minXPercent) / 2), p(height, (maxYPercent + minYPercent) / 2));
        }
        return RNG.point(p(width, minXPercent), p(width, maxXPercent), p(height, minYPercent), p(height, maxYPercent));
    }

    private static int p(int dimension, int percent) {
        return (int) (dimension * (percent / 100f));
    }
}
