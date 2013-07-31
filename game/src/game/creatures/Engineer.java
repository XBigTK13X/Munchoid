package game.creatures;

import game.GameConfig;
import game.creatures.style.BodyRules;
import sps.core.RNG;

public class Engineer {
    public static BodyParts designParts(Body body) {
        BodyParts result = new BodyParts(body);

        int numberOfParts = RNG.next(GameConfig.MinBodyParts, GameConfig.MaxBodyParts);
        int partWidthMin = (int) GameConfig.MinBodyPartSize.X;
        int partHeightMin = (int) GameConfig.MinBodyPartSize.Y;
        int partWidthMax = (int) GameConfig.MaxBodyPartSize.X;
        int partHeightMax = (int) GameConfig.MaxBodyPartSize.Y;
        for (int ii = 0; ii < numberOfParts; ii++) {
            BodyPart parent = result.getAParent();
            PartFunction function = BodyRules.getChildFunction(parent);
            BodyPart part = new BodyPart(function, RNG.next((int) (partWidthMin * function.Mult), (int) (partWidthMax * function.Mult)), RNG.next((int) (partHeightMin * function.Mult), (int) (partHeightMax * function.Mult)), body);
            if (parent != null) {
                parent.addChild(part);
                result.assignDepth(part);
            }
            result.add(part);
        }

        return result;
    }
}
