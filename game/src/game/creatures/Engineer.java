package game.creatures;

import game.GameConfig;
import game.creatures.part.DebugPoly;
import game.creatures.part.Design;
import game.creatures.part.Designs;
import sps.core.Logger;
import sps.core.RNG;

public class Engineer {
    private static final int pwMin = (int) GameConfig.MinBodyPartSize.X;
    private static final int pHMin = (int) GameConfig.MinBodyPartSize.Y;
    private static final int pWMax = (int) GameConfig.MaxBodyPartSize.X;
    private static final int pHMax = (int) GameConfig.MaxBodyPartSize.Y;

    public static BodyParts designParts(Body body) {
        int maxParts = RNG.next(GameConfig.MinBodyParts, GameConfig.MaxBodyParts);
        BodyParts result = new BodyParts(body);
        BodyPart core = construct(body, PartFunction.Core, null);

        while (GameConfig.MinBodyParts > result.getAll().size()) {
            fill(maxParts, body, result, core);
        }

        if (GameConfig.DevDrawSkeleton) {
            Logger.info(result.getCore().getJoints().debug());
        }

        return result;
    }

    private static BodyPart construct(Body owner, PartFunction f, Design design) {
        if (!GameConfig.DevDebugJointGrid || f == PartFunction.Core) {
            design = Designs.get(f);
        }
        return new BodyPart(f, RNG.next((int) (pwMin * f.Mult), (int) (pWMax * f.Mult)), RNG.next((int) (pHMin * f.Mult), (int) (pHMax * f.Mult)), owner, design);
    }

    private static void fill(int maxParts, Body owner, BodyParts result, BodyPart parent) {
        if (maxParts <= result.getAll().size()) {
            return;
        }
        result.add(parent);
        for (Joint j : parent.getJoints().getAll()) {
            if (maxParts <= result.getAll().size()) {
                return;
            }
            if (RNG.coinFlip()) {
                PartFunction f = PartFunction.random(j.GridLoc, parent.getFunction());
                if (f != null) {
                    Design design = Designs.get(f);
                    if (GameConfig.DevDebugJointGrid) {
                        design = new DebugPoly(j.GridLoc);
                    }
                    BodyPart child = construct(owner, f, design);
                    child.setParent(parent, j);
                    fill(maxParts, owner, result, child);
                }
            }
        }
    }
}
