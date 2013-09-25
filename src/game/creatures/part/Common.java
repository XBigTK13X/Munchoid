package game.creatures.part;

public class Common {
    public static int[][] rectangle(int width, int height) {
        int[][] result = new int[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                result[ii][jj] = Design.BaseColor;
            }
        }
        return result;
    }

    public static int[][] trim(int[][] design) {
        int leftX = 0;
        boolean found = false;
        for (int ii = 0; ii < design.length; ii++) {
            for (int jj = 0; jj < design[0].length; jj++) {
                if (design[ii][jj] != Design.Empty) {
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
                if (design[ii][jj] != Design.Empty) {
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
                if (design[jj][ii] != Design.Empty) {
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
                if (design[jj][ii] != Design.Empty) {
                    topY = ii + 1;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int[][] trimmed = new int[rightX - leftX][topY - bottomY];
        int tI = 0;
        int tJ = 0;
        for (int ii = leftX; ii < rightX; ii++) {
            for (int jj = bottomY; jj < topY; jj++) {
                int a = design[ii][jj];
                trimmed[tI][tJ] = a;
                tJ++;
            }
            tJ = 0;
            tI++;
        }
        return trimmed;
    }

    public static int[][] empty(int[][] arr) {
        for (int ii = 0; ii < arr.length; ii++) {
            for (int jj = 0; jj < arr[0].length; jj++) {
                arr[ii][jj] = Design.Empty;
            }
        }
        return arr;
    }

    public static int[][] invert(int[][] arr) {
        for (int ii = 0; ii < arr.length; ii++) {
            for (int jj = 0; jj < arr[0].length; jj++) {
                if (arr[ii][jj] == Design.BaseColor) {
                    arr[ii][jj] = Design.Empty;
                }
                else if (arr[ii][jj] == Design.Empty) {
                    arr[ii][jj] = Design.BaseColor;
                }

            }
        }
        return arr;
    }
}
