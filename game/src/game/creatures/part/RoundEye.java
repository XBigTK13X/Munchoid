package game.creatures.part;

import sps.entities.HitTest;

public class RoundEye implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        int dim = Math.min(width, height);
        boolean[][] result = Common.empty(Common.rectangle(dim, dim));


        float lidRad2Min = (float) Math.pow(dim * .35, 2);
        float lidRad2Max = (float) Math.pow(dim * .5, 2);
        float pupilRad2 = (float) Math.pow(dim * .1, 2);
        for (int ii = 0; ii < dim; ii++) {
            for (int jj = 0; jj < dim; jj++) {
                float rad2 = HitTest.getDistanceSquare(dim / 2, ii, dim / 2, jj);
                if (rad2 >= lidRad2Min && rad2 <= lidRad2Max) {
                    result[ii][jj] = true;
                }

                float pupCenterRad2 = HitTest.getDistanceSquare(dim * .66f, ii, dim / 2, jj);
                if (pupCenterRad2 <= pupilRad2) {
                    result[ii][jj] = true;
                }
            }
        }

        return result;
    }


}
