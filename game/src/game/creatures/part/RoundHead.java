package game.creatures.part;

import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;

public class RoundHead implements Design{
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = new boolean[width][height];

        double radius = Math.min(width,height);
        Point2 center = new Point2(width/2,height/2);
        for(int ii = 0;ii<width;ii++){
            for(int jj = 0;jj<height;jj++){
                if(HitTest.getDistanceSquare(ii,center.X,jj,center.Y) <= radius * radius){
                    result[ii][jj] = true;
                }
            }
        }

        return result;
    }
}
