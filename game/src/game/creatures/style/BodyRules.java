package game.creatures.style;

import game.creatures.BodyPart;
import game.creatures.PartFunction;
import sps.core.Point2;
import sps.core.RNG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyRules {
    public static Map<PartFunction, List<PartFunction>> __supports = new HashMap<PartFunction, List<PartFunction>>();

    static {
        __supports.put(PartFunction.Body, Arrays.asList(PartFunction.BodyDetail, PartFunction.UpperLimb, PartFunction.LowerLimb, PartFunction.Head));
        __supports.put(PartFunction.Head, Arrays.asList(PartFunction.HeadDetail));
    }


    public static PartFunction getChildFunction(BodyPart parent) {
        if (parent == null) {
            return PartFunction.Body;
        }
        return __supports.get(parent.getFunction()).get(RNG.next(0, __supports.get(parent.getFunction()).size()));
    }

    public static boolean supports(PartFunction function) {
        return __supports.containsKey(function);
    }

    //Each part is divided into a 3x3 grid.
    //Children are placed randomly within any point
    //that lies within that part function's possible grid locs
    public static Point2 getOrigin(BodyPart part) {
        BodyPart parent = part.getParent();
        if (parent == null) {
            return new Point2(0, 0);
        }
        //Math to convert a grid location (1->9) into coordinate ranges.
        // For example, location 1 corresponds to the range (0,0)->(33,33)
        // Using this calculation makes it easier to break the grid into smaller pieces
        // and increase control over part placement

        //Subtracting here inverts the Y axis
        int i = PartFunction.GridSize + 1 - (Integer) RNG.pick(part.getFunction().GridLocs);
        int j = i - 1;
        int n = PartFunction.GridSize / 3;
        float m = 100 / (float) (n);
        int xMin = (int) (m * (j % n));
        int yMin = (int) (m * Math.floor(j / n));
        int xMax = (int) (m * (1 + (j % n)));
        int yMax = (int) (m * (1 + (j / n)));
        return pointInside(parent.getWidth(), parent.getHeight(), xMin, xMax, yMin, yMax);
    }

    private static Point2 pointInside(int width, int height, int minXPercent, int maxXPercent, int minYPercent, int maxYPercent) {
        return RNG.point(p(width, minXPercent), p(width, maxXPercent), p(height, minYPercent), p(height, maxYPercent));
    }

    private static int p(int dimension, int percent) {
        return (int) (dimension * (percent / 100f));
    }
}
