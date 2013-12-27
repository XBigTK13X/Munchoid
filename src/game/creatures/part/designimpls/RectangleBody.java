package game.creatures.part.designimpls;

import game.creatures.part.Common;
import game.creatures.part.Design;

public class RectangleBody implements Design {
    @Override
    public int[][] create(int width, int height) {
        return Common.rectangle(width, height);
    }
}
