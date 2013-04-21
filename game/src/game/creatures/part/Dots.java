package game.creatures.part;

import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;

public class Dots implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = new boolean[width][height];
        int numDots = RNG.next(3, 6);
        for (int ii = 0; ii < numDots; ii++) {
            int radius = RNG.next(1, width * height / numDots);
            Point2 center = RNG.point(0, width, 0, height);
            for (int jj = (int) center.X - radius; jj < center.X + radius; jj++) {
                for (int kk = (int) center.Y - radius; kk < center.Y + radius; kk++) {
                    if (jj > 0 && kk > 0 && jj < result.length && kk < result[0].length) {
                        if (HitTest.getDistanceSquare(jj, center.X, kk, center.Y) <= radius * radius) {
                            result[jj][kk] = true;
                        }
                    }
                }
            }
        }
        return result;
    }
}
