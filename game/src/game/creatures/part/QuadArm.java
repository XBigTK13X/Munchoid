package game.creatures.part;

public class QuadArm implements Design {
    @Override
    public boolean[][] create(int width, int height) {
        boolean[][] result = new boolean[width][height];
        int armHeight = height / 5;
        if (armHeight == 0) {
            armHeight = 1;
        }
        for (int ii = 0; ii < width; ii++) {
            for (int jj = armHeight; jj < armHeight * 2; jj++) {
                result[ii][jj] = true;
            }
        }

        int handHeight = (int) (armHeight * 1.5);
        handHeight = handHeight > height ? height : handHeight;
        for (int ii = width - armHeight; ii < width; ii++) {
            for (int jj = armHeight - armHeight / 2; jj < armHeight + handHeight; jj++) {
                result[ii][jj] = true;
            }
        }
        return result;
    }
}
