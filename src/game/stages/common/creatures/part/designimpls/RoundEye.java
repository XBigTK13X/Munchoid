package game.stages.common.creatures.part.designimpls;

import game.stages.common.creatures.part.Common;
import game.stages.common.creatures.part.Design;
import sps.entities.HitTest;

public class RoundEye implements Design {
    @Override
    public int[][] create(int width, int height) {
        int dim = Math.min(width, height);
        int[][] result = Common.empty(Common.rectangle(dim, dim));

        float lidRadMin = dim * .35f;
        float lidRadMax = dim * .5f;
        float pupilRad = dim * .1f;
        for (int ii = 0; ii < dim; ii++) {
            for (int jj = 0; jj < dim; jj++) {
                float rad = HitTest.getDistance(dim / 2, dim / 2, ii, jj);
                if (rad >= lidRadMin && rad <= lidRadMax) {
                    result[ii][jj] = Design.BaseColor;
                }

                float pupCenterRad = HitTest.getDistance(dim * .66f, dim / 2, ii, jj);
                if (pupCenterRad <= pupilRad) {
                    result[ii][jj] = Design.BaseColor;
                }

                if (pupCenterRad > pupilRad && rad < lidRadMin) {
                    result[ii][jj] = Design.White;
                }
            }
        }

        return result;
    }
}
