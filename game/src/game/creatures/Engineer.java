package game.creatures;

import game.GameConfig;
import sps.core.Logger;
import sps.core.RNG;

public class Engineer {
    public static BodyParts designParts(Body body) {
        Logger.info("NEW");

        BodyParts result = new BodyParts(body);

        int numberOfParts = RNG.next(GameConfig.MinBodyParts, GameConfig.MaxBodyParts);
        int pwMin = (int) GameConfig.MinBodyPartSize.X;
        int pHMin = (int) GameConfig.MinBodyPartSize.Y;
        int pWMax = (int) GameConfig.MaxBodyPartSize.X;
        int pHMax = (int) GameConfig.MaxBodyPartSize.Y;

        for (int ii = 0; ii < numberOfParts; ii++) {
            BodyPart parent = result.getAnOpenParent();
            PartFunction f = PartFunction.Body;
            Connection openLoc = null;
            if (parent != null) {
                openLoc = parent.getConnections().getOpenJoint();
                f = PartFunction.random(openLoc.GridLoc, parent.getFunction());
            }
            BodyPart part = new BodyPart(f, RNG.next((int) (pwMin * f.Mult), (int) (pWMax * f.Mult)), RNG.next((int) (pHMin * f.Mult), (int) (pHMax * f.Mult)), body);

            if (f != PartFunction.Body) {
                parent.addChild(part, openLoc);
                result.assignDepth(part);
            }
            result.add(part);
        }

        return result;
    }
}
