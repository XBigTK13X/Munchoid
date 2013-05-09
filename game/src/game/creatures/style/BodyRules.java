package game.creatures.style;

import game.creatures.BodyPart;
import game.creatures.PartFunction;
import sps.core.Logger;
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

    public static BodyPart getParent(List<BodyPart> parts) {
        if (parts.size() == 0) {
            return null;
        }
        while (true) {
            BodyPart target = parts.get(RNG.next(0, parts.size()));
            if (__supports.containsKey(target.getFunction())) {
                return target;
            }
        }
    }

    public static PartFunction getChildFunction(BodyPart parent) {
        if (parent == null) {
            return PartFunction.Body;
        }
        return __supports.get(parent.getFunction()).get(RNG.next(0, __supports.get(parent.getFunction()).size()));
    }

    private static Point2 pointInside(int width, int height, int minXPercent, int maxXPercent, int minYPercent, int maxYPercent) {
        return RNG.point(p(width, minXPercent), p(width, maxXPercent), p(height, minYPercent), p(height, maxYPercent));
    }

    private static int p(int dimension, int percent) {
        return (int) (dimension * (percent / 100f));
    }

    private static class Contrib {
        public final int x;
        public final int X;
        public final int y;
        public final int Y;

        public Contrib(int x, int X, int y, int Y) {
            //Rng.point will throw an exception if min > max
            if (X < x) {
                int t = x;
                x = X;
                X = t;
            }
            if (Y < y) {
                int t = y;
                y = Y;
                Y = t;
            }
            this.y = y;
            this.Y = Y;
            this.x = x;
            this.X = X;
        }
    }

    public static Point2 getOrigin(BodyPart part) {
        Contrib C = null;
        Contrib c = null;
        BodyPart parent = part.getParent();
        if (parent == null) {
            return new Point2(0, 0);
        }
        int wdP = (int) (((parent.getWidth() - part.getWidth()) / (float) parent.getWidth()) * 100);

        int hdP = (int) (((parent.getHeight() - part.getHeight()) / (float) parent.getHeight()) * 100);

        Logger.info("Parent: " + part.getParent().getFunction() + ", Child: " + part.getFunction());
        //Position on parent
        switch (part.getFunction()) {
            case Head:
                C = new Contrib(0, 100, 50, 50);
                c = new Contrib(0, 0, 0, 50);
                break;
            case UpperLimb:
                C = new Contrib(100, 100, 50, 50);
                c = new Contrib(-33, 25, -33, 33);
                break;
            case LowerLimb:
                C = new Contrib(0, wdP, 0, 20);
                c = new Contrib(0, 0, -100, -100);
                break;
            case BodyDetail:
                C = new Contrib(0, wdP, 0, hdP);
                c = new Contrib(0, 0, 0, 0);
                break;
            case HeadDetail:
                C = new Contrib(0, wdP, 0, 0);
                c = new Contrib(0, 0, 33, 100);
                break;
            case Body:
                return new Point2(0, 0);
            default:
                Logger.info("Case not handled while positioning a body part: " + part.getFunction());
                break;
        }
        return pointInside(parent.getWidth(), parent.getHeight(), C.x, C.X, C.y, C.Y).addRaw(pointInside(part.getWidth(), part.getHeight(), c.x, c.X, c.y, c.Y));
    }
}
