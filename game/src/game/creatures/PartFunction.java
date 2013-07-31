package game.creatures;

public enum PartFunction {
    Body(2, 7, 1),
    Head(1, 4, 1, 2, 3),
    UpperLimb(1, 1, 1, 3, 4, 6),
    LowerLimb(1, 1, 3, 6, 7, 9),
    BodyDetail(1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
    HeadDetail(.7f, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    //Must be a square number
    public static final int GridSize = 9;

    public final float Mult;
    public final int MaxChildPerJoint;
    public final int MaxConnections;
    public final Integer[] GridLocs;

    private PartFunction(float sizeMultiplier, int maxConnections, Integer... gridLocs) {
        Mult = sizeMultiplier;
        MaxConnections = maxConnections;
        //TODO Supprt for multiple children per joint;
        MaxChildPerJoint = 1;
        GridLocs = gridLocs;
    }
}
