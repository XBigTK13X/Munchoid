package game.creatures;

import sps.core.RNG;

//TODO Base position for each part?

public enum PartFunction {
    Body(1),
    Head(1),
    UpperLimb(1),
    LowerLimb(1),
    BodyDetail(1),
    HeadDetail(1);

    public final int Mult;

    private PartFunction(int sizeMultiplier) {
        Mult = sizeMultiplier;
    }

    public static PartFunction nonBody() {
        return values()[RNG.next(1, values().length)];
    }
}
