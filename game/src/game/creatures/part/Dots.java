package game.creatures.part;

import sps.core.RNG;

public class Dots implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = new boolean[width][height];
        for (int ii = 0; ii < result.length; ii++) {
            for (int jj = 0; jj < result[0].length; jj++) {
                result[ii][jj] = RNG.percent(7);
            }
        }
        return result;
    }
}
