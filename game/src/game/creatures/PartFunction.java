package game.creatures;

public enum PartFunction {
    Body(2, 7),
    Head(1, 4),
    UpperLimb(1, 1),
    LowerLimb(1, 1),
    BodyDetail(1, 0),
    HeadDetail(.7f, 0);

    public final float Mult;
    public final int MaxChildren;

    private PartFunction(float sizeMultiplier, int maxChildren) {
        Mult = sizeMultiplier;
        MaxChildren = maxChildren;
    }
}
