package game.creatures;

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
        MaxChildPerJoint = 1;
    }

    public static Integer[] parentLocations(PartFunction function) {
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

    public static Integer[] jointLocations(PartFunction function) {
        switch (function) {
            case Body:
                return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            case Head:
                return new Integer[]{1, 2, 3, 5};
            case UpperLimb:
                return new Integer[]{2, 8};
            case LowerLimb:
                return new Integer[]{2, 8};
            case BodyDetail:
                return new Integer[]{5};
            case HeadDetail:
                return new Integer[]{5};
            default:
                throw new RuntimeException("partFunction with undefined jointGridLocs: " + function);
        }
    }
}
