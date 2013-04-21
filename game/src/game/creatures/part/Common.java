package game.creatures.part;

public class Common {
    public static boolean[][] rectangle(int width, int height) {
        boolean[][] result = new boolean[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                result[ii][jj] = true;
            }
        }
        return result;
    }

    public static boolean[][] trim(boolean[][] design) {
        int leftX = 0;
        boolean found = false;
        for (int ii = 0; ii < design.length; ii++) {
            for (int jj = 0; jj < design[0].length; jj++) {
                if (design[ii][jj]) {
                    leftX = ii;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int rightX = 0;
        found = false;
        for (int ii = design.length - 1; ii > 0; ii--) {
            for (int jj = design[0].length - 1; jj > 0; jj--) {
                if (design[ii][jj]) {
                    rightX = ii + 1;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int bottomY = 0;
        found = false;
        for (int ii = 0; ii < design[0].length; ii++) {
            for (int jj = 0; jj < design.length; jj++) {
                if (design[jj][ii]) {
                    bottomY = ii;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int topY = 0;
        found = false;
        for (int ii = design[0].length - 1; ii > 0; ii--) {
            for (int jj = design.length - 1; jj > 0; jj--) {
                if (design[jj][ii]) {
                    topY = ii + 1;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        boolean[][] trimmed = new boolean[rightX - leftX][topY - bottomY];
        int tI = 0;
        int tJ = 0;
        for (int ii = leftX; ii < rightX; ii++) {
            for (int jj = bottomY; jj < topY; jj++) {
                boolean a = design[ii][jj];
                trimmed[tI][tJ] = a;
                tJ++;
            }
            tJ = 0;
            tI++;
        }
        return trimmed;
    }

    public static boolean[][] empty(boolean[][] arr) {
        for (int ii = 0; ii < arr.length; ii++) {
            for (int jj = 0; jj < arr[0].length; jj++) {
                arr[ii][jj] = false;
            }
        }
        return arr;
    }
}
