package game.creatures;

public enum PartFunction {
    Body(2),
    Head(1),
    UpperLimb(1),
    LowerLimb(1),
    BodyDetail(1),
    HeadDetail(.7f);

    public final float Mult;

    private PartFunction(float sizeMultiplier) {
        Mult = sizeMultiplier;
    }
}
