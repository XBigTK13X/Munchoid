package game.creatures.part;

public class QuadLeg implements Design {
    @Override
    public int[][] create(int width, int height) {
        int[][] result = new int[width][height];
        int legWidth = width / 4;
        if (legWidth == 0) {
            legWidth = 1;
        }
        for (int ii = 0; ii < legWidth; ii++) {
            for (int jj = 0; jj < height; jj++) {
                result[ii][jj] = Design.BaseColor;
            }
        }
        int footWidth = (int) (legWidth * 1.5f);
        footWidth = footWidth > width ? width : footWidth;
        for (int ii = 0; ii < footWidth; ii++) {
            for (int jj = 0; jj < footWidth; jj++) {
                if (ii < result.length && jj < result[0].length) {
                    result[ii][jj] = Design.BaseColor;
                }
            }
        }
        return result;
    }
}
