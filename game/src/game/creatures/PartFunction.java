package game.creatures;

import sps.core.Logger;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PartFunction {
    Body(2, 7, 0),
    Head(1, 4, 0),
    UpperLimb(1, 1, 15),
    LowerLimb(1, 1, 90),
    HeadDetail(.7f, 0, 10);

    //Must be a square number
    public static final int GridSize = 9;

    public final float Mult;
    public final int MaxChildPerJoint;
    public final int MaxConnections;
    public final int RotationOffset;

    private PartFunction(float sizeMultiplier, int maxConnections, int rotationOffset) {
        Mult = sizeMultiplier;
        MaxConnections = maxConnections;
        MaxChildPerJoint = 1;
        RotationOffset = rotationOffset;
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
            case HeadDetail:
                return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            default:
                throw new RuntimeException("partFunction with undefined parentGridLocs: " + function);
        }
    }


    private static Map<PartFunction, Integer[]> __joints;
    private static Map<Integer, List<PartFunction>> __parents;
    private static Map<PartFunction, PartFunction[]> __possibleChildren;

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
            __joints.put(Body, new Integer[]{1, 2, 3, 4, 6, 7, 8, 9});
            __joints.put(Head, new Integer[]{1, 2, 3, 5});
            __joints.put(UpperLimb, new Integer[]{2, 8});
            __joints.put(LowerLimb, new Integer[]{2, 8});
            __joints.put(HeadDetail, new Integer[]{5});
            __possibleChildren = new HashMap<PartFunction, PartFunction[]>();
            __possibleChildren.put(Body, new PartFunction[]{UpperLimb, LowerLimb, Head});
            __possibleChildren.put(Head, new PartFunction[]{HeadDetail});
            __possibleChildren.put(UpperLimb, new PartFunction[]{UpperLimb});
            __possibleChildren.put(LowerLimb, new PartFunction[]{LowerLimb});
        }
        return __joints.get(function);
    }

    public static PartFunction random(int parentJoint, PartFunction parent) {
        int maxTries = 100;
        while (maxTries-- > 0) {
            PartFunction f = __parents.get(parentJoint).get(RNG.next(__parents.get(parentJoint).size()));
            Logger.info("f: " + f + ", " + parent);
            if (possibleChild(parent, f)) {
                return f;
            }
        }
        throw new RuntimeException("Unable to find a suitable child joint");
    }

    public static boolean possibleChild(PartFunction parent, PartFunction child) {
        for (PartFunction f : __possibleChildren.get(parent)) {
            if (f == child) {
                return true;
            }
        }
        return false;
    }
}
