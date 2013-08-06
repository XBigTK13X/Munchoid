package game.creatures;

import sps.core.RNG;

import java.util.HashMap;
import java.util.Map;

public enum PartFunction {
    Body(2, 7, 0, 0),
    Head(1, 4, 0, 2),
    UpperLimb(1, 1, 15, 2),
    LowerLimb(1, 1, 90, 2),
    HeadDetail(.7f, 0, 10, 5);

    //Must be a square number
    public static final int GridSize = 9;
    public static final int GridLocFraction = (int) Math.sqrt(GridSize);

    public final float Mult;
    public final int MaxChildPerJoint;
    public final int MaxConnections;
    public final int RotationOffset;
    public final int LocAttach;

    private PartFunction(float sizeMultiplier, int maxConnections, int rotationOffset, int gridLocAttach) {
        Mult = sizeMultiplier;
        MaxConnections = maxConnections;
        MaxChildPerJoint = 1;
        RotationOffset = rotationOffset;
        LocAttach = gridLocAttach;
    }

    private static Map<PartFunction, Integer[]> __joints;
    private static Map<PartFunction, PartFunction[]> __possibleChildren;

    public static Integer[] jointLocations(PartFunction function) {
        if (__joints == null) {
            __joints = new HashMap<PartFunction, Integer[]>();
            __joints.put(Body, new Integer[]{1, 2, 3, 4, 6, 7, 8, 9});
            __joints.put(Head, new Integer[]{1, 2, 3, 5});
            __joints.put(UpperLimb, new Integer[]{8});
            __joints.put(LowerLimb, new Integer[]{8});
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
        if (!__possibleChildren.containsKey(parent)) {
            return null;
        }
        return __possibleChildren.get(parent)[RNG.next(__possibleChildren.get(parent).length)];
    }
}
