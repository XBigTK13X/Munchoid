package game.creatures;

import sps.core.RNG;

//TODO Base position for each part?

public enum PartFunction {
    Body,
    Head,
    UpperLimb,
    LowerLimb,
    BodyDetail,
    HeadDetail;

    public static PartFunction nonBody() {
        return values()[RNG.next(1,values().length)];
    }
}
