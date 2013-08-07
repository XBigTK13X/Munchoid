package game.creatures;

import sps.core.RNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PartFunction {
    Core(1.5f, 7, 0, 0),
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
    private static Map<PartFunction, Integer[]> __parentJoints;
    private static Map<PartFunction, PartFunction[]> __childFunctions;

    public static Integer[] jointLocations(PartFunction function) {
        return __joints.get(function);
    }


    public static void initJointSpecs() {
        if (__joints == null) {
            //The grid locations on a part where a child can be added
            __joints = new HashMap<PartFunction, Integer[]>();
            __joints.put(Core, new Integer[]{1, 2, 3, 4, 6, 7, 8, 9});
            __joints.put(Head, new Integer[]{5, 6});
            __joints.put(UpperLimb, new Integer[]{2, 8});
            __joints.put(LowerLimb, new Integer[]{2, 8});
            __joints.put(HeadDetail, new Integer[]{5});

            //What a part's children can be
            //NULL means a part has no ability to host a child
            __childFunctions = new HashMap<PartFunction, PartFunction[]>();
            __childFunctions.put(Core, new PartFunction[]{UpperLimb, LowerLimb, Head});
            __childFunctions.put(Head, new PartFunction[]{HeadDetail});
            __childFunctions.put(UpperLimb, new PartFunction[]{UpperLimb});
            __childFunctions.put(LowerLimb, new PartFunction[]{LowerLimb});
            __childFunctions.put(HeadDetail, null);

            //The places on a parent where a function may connect
            __parentJoints = new HashMap<PartFunction, Integer[]>();
            __parentJoints.put(Core, null);
            __parentJoints.put(Head, new Integer[]{7, 8, 9});
            __parentJoints.put(UpperLimb, new Integer[]{4, 5, 6});
            __parentJoints.put(LowerLimb, new Integer[]{1, 2, 3});
            __parentJoints.put(HeadDetail, new Integer[]{5, 6});
        }

        for (PartFunction pf : values()) {
            if (!__joints.containsKey(pf) || !__childFunctions.containsKey(pf) || !__parentJoints.containsKey(pf)) {
                throw new RuntimeException("Joint specs are incomplete for: " + pf.name());
            }
        }
    }

    public static PartFunction random(int parentJoint, PartFunction parent) {
        PartFunction[] validChildren = __childFunctions.get(parent);
        if (validChildren == null) {
            return null;
        }
        List<PartFunction> childrenWithCorrectAttachment = new ArrayList<PartFunction>();
        for (PartFunction child : validChildren) {
            for (Integer gridLoc : __parentJoints.get(child)) {
                if (gridLoc == parentJoint) {
                    childrenWithCorrectAttachment.add(child);
                }
            }
        }
        if (childrenWithCorrectAttachment.size() == 0) {
            return null;
        }
        return childrenWithCorrectAttachment.get(RNG.next(childrenWithCorrectAttachment.size()));
    }
}
