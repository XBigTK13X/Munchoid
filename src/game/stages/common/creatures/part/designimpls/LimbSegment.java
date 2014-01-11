package game.stages.common.creatures.part.designimpls;

import game.stages.common.creatures.part.Common;
import game.stages.common.creatures.part.Design;

public class LimbSegment implements Design {
    @Override
    public int[][] create(int width, int height) {
        return Common.rectangle(Math.min(width, height), Math.min(width, height) / 2);
    }
}
