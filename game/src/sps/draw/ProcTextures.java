package sps.draw;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.core.RNG;

public class ProcTextures {
    private static final int defaultPerlinSmoothness = 6;

    public static Color[][] genArr(int width, int height, Color color) {
        Color[][] base = new Color[width][height];
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = color;
            }
        }
        return base;
    }

    public static void remove(Color[][] arr, Color color) {
        for (int ii = 0; ii < arr.length; ii++) {
            for (int jj = 0; jj < arr[ii].length; jj++) {
                if (arr[ii][jj] == color) {
                    arr[ii][jj] = null;
                }
            }
        }
    }

    public static Color[][] genPerlinGrid(int width, int height, Color start, Color end) {
        return genPerlinGrid(width, height, start, end, RNG.next(ProcTextures.defaultPerlinSmoothness - 1, ProcTextures.defaultPerlinSmoothness + 1));
    }

    public static Color[][] genPerlinGrid(int width, int height, Color start, Color end, int smoothness) {
        if (GameConfig.OptDisableCloudyTextures) {
            Color[][] result = new Color[width][height];
            for (int ii = 0; ii < width; ii++) {
                for (int jj = 0; jj < height; jj++) {
                    result[ii][jj] = start;
                }
            }
            return result;
        }

        float[][] noise = Noise.perlin(width, height, smoothness);
        return Noise.mapGradient(start, end, noise);
    }
}
