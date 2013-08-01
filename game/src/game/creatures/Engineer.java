package game.creatures;

import game.GameConfig;
import sps.core.Logger;
import sps.core.RNG;

public class Engineer {
    private static final int pwMin = (int) GameConfig.MinBodyPartSize.X;
    private static final int pHMin = (int) GameConfig.MinBodyPartSize.Y;
    private static final int pWMax = (int) GameConfig.MaxBodyPartSize.X;
    private static final int pHMax = (int) GameConfig.MaxBodyPartSize.Y;

    public static BodyParts designParts(Body body) {
        Logger.info("Creating a new body");

        int maxParts = RNG.next(GameConfig.MinBodyParts, GameConfig.MaxBodyParts);
        BodyParts result = new BodyParts(body);
        BodyPart core = construct(body, PartFunction.Body);

        fill(maxParts, body, result, core);


        return result;
    }

    private static BodyPart construct(Body owner, PartFunction f) {
        return new BodyPart(f, RNG.next((int) (pwMin * f.Mult), (int) (pWMax * f.Mult)), RNG.next((int) (pHMin * f.Mult), (int) (pHMax * f.Mult)), owner);
    }

    private static void fill(int maxParts, Body owner, BodyParts result, BodyPart parent) {
        result.add(parent);
        if (maxParts <= result.getAll().size()) {
            return;
        }
        for (Joint j : parent.getJoints().getAll()) {
            PartFunction f = PartFunction.random(j.GridLoc, parent.getFunction());
            if (f != null) {
                BodyPart child = construct(owner, f);
                j.setChild(child);
                child.setParent(parent);
                fill(maxParts, owner, result, child);
            }
        }
    }
}
