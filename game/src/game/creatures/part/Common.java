package game.creatures.part;

import game.creatures.Atom;

public class Common {
    public static boolean[][] rectangle(int width, int height){
        boolean[][] result = new boolean[width][height];
        for(int ii = 0;ii<width;ii++){
            for(int jj = 0;jj<height;jj++){
                result[ii][jj] = true;
            }
        }
        return result;
    }
}
