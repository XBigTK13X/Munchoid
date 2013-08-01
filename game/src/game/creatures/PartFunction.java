package game.creatures;

import sps.core.RNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PartFunction {
    Body(2, 7),
    Head(1, 4),
    UpperLimb(1, 1),
    LowerLimb(1, 1),
    BodyDetail(1, 0),
    HeadDetail(.7f, 0);

    //Must be a square number
    public static final int GridSize = 9;

    public final float Mult;
    public final int MaxChildPerJoint;
    public final int MaxConnections;

    private PartFunction(float sizeMultiplier, int maxConnections) {
        Mult = sizeMultiplier;
        MaxConnections = maxConnections;
        //TODO Supprt for multiple children per joint;
        MaxChildPerJoint = 10;
    }

    private static Integer[] parentLocations(PartFunction function) {
        switch (function) {
            case Body:
                return new Integer[]{1};
            case Head:
                return new Integer[]{1, 2, 3};
            case UpperLimb:
                return new Integer[]{1, 3, 4, 6};
            case LowerLimb:
                return new Integer[]{3, 6, 7, 9};
            case BodyDetail:
                return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            case HeadDetail:
                return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            default:
                throw new RuntimeException("partFunction with undefined parentGridLocs: " + function);
        }
    }


    private static Map<PartFunction, Integer[]> __joints;
    private static Map<Integer, List<PartFunction>> __parents;

    private static void ingestParents() {
        __parents = new HashMap<Integer, List<PartFunction>>();
        for (PartFunction f : values()) {
            Integer[] locs = parentLocations(f);
            for (Integer i : locs) {
                if (!__parents.containsKey(i)) {
                    __parents.put(i, new ArrayList<PartFunction>());
                }
                __parents.get(i).add(f);
            }
        }
    }

    public static Integer[] jointLocations(PartFunction function) {
        if (__joints == null) {
            ingestParents();
            __joints = new HashMap<PartFunction, Integer[]>();
            __joints.put(Body, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
            __joints.put(Head, new Integer[]{1, 2, 3, 5});
            __joints.put(UpperLimb, new Integer[]{2, 8});
            __joints.put(LowerLimb, new Integer[]{2, 8});
            __joints.put(BodyDetail, new Integer[]{5});
            __joints.put(HeadDetail, new Integer[]{5});
        }
        return __joints.get(function);
    }

    public static PartFunction random(int parentJoint) {
        return __parents.get(parentJoint).get(RNG.next(__parents.get(parentJoint).size()));
    }
}
