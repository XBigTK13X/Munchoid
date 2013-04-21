package game.creatures.part;

public class QuadLeg implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = new boolean[width][height];
        int legWidth = width / 4;
        if (legWidth == 0) {
            legWidth = 1;
        }
        for (int ii = 0; ii < legWidth;ii++) {
            for(int jj=0;jj<height;jj++){
                result[ii][jj] = true;
            }
        }
        for (int ii = 0; ii < width;ii++) {
            for(int jj=height-legWidth;jj<height;jj++){
                result[ii][jj] = true;
            }
        }
        return result;
    }
}
