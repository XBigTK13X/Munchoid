package sps.draw;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;
import sps.util.MathHelper;

public class ProcTextures {
    private static final int defaultPerlinSmoothness = 6;

    public static void remove(Color[][] arr, Color color) {
        for (int ii = 0; ii < arr.length; ii++) {
            for (int jj = 0; jj < arr[ii].length; jj++) {
                if (arr[ii][jj] == color) {
                    arr[ii][jj] = null;
                }
            }
        }
    }

    public static Color[][] monotone(int width, int height, Color color) {
        Color[][] base = new Color[width][height];
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = color;
            }
        }
        return base;
    }

    public static Color[][] perlin(int width, int height, Color start, Color end) {
        return perlin(width, height, start, end, RNG.next(ProcTextures.defaultPerlinSmoothness - 1, ProcTextures.defaultPerlinSmoothness + 1));
    }

    public static Color[][] perlin(int width, int height, Color start, Color end, int smoothness) {
        return perlin(width, height, start, end, smoothness, false);
    }

    public static Color[][] perlin(int width, int height, Color start, Color end, int smoothness, boolean ignoreOptimizationsConfig) {
        if (GameConfig.OptDisableCloudyTextures && !ignoreOptimizationsConfig) {
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

    public static Color[][] gradient(int width, int height, Color start, Color end, boolean vertical) {
        Color[][] result = new Color[width][height];
        Color[] g = Colors.gradient(start, end, vertical ? height : width);
        for (int ii = 0; ii < result.length; ii++) {
            for (int jj = 0; jj < result[0].length; jj++) {
                result[ii][jj] = g[vertical ? jj : ii];
            }
        }
        return result;
    }

    public static Color[][] radial(int width, int height, Color start, Color end) {
        return radial(width, height, start, end, RNG.point(0, width, 0, height));
    }

    public static Color[][] radial(int width, int height, Color start, Color end, Point2 center) {
        Color[][] result = new Color[width][height];
        Color[] g = Colors.gradient(start, end, (int) Math.sqrt(height * height + width * width));


        for (int ii = 0; ii < result.length; ii++) {
            for (int jj = 0; jj < result[0].length; jj++) {
                int dist = (int) HitTest.getDistance(ii, jj, center.X, center.Y);
                result[ii][jj] = g[MathHelper.clamp(dist, 0, g.length)];
            }
        }

        return result;
    }
}
