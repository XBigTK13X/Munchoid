package game.stages.common.creatures.part.designimpls;

import game.stages.common.creatures.part.Common;
import game.stages.common.creatures.part.Design;

public class RectangleBody implements Design {
    @Override
    public int[][] create(int width, int height) {
        return Common.rectangle(width, height);
    }
}
