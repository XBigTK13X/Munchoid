package sps.draw;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.core.RNG;

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

    public static void negative(Color[][] base) {
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = new Color(1f - base[ii][jj].r, 1f - base[ii][jj].g, 1f - base[ii][jj].b, 1f - base[ii][jj].a);
            }
        }
    }

    public static void blur(Color[][] base, int blurriness) {
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                float r = 0;
                float g = 0;
                float b = 0;
                float a = 0;
                int hits = 0;
                for (int kk = -blurriness; kk < blurriness; kk++) {
                    for (int ll = -blurriness; ll < blurriness; ll++) {
                        if (kk != 0 || ll != 0) {
                            int x = ii + kk;
                            int y = jj + ll;
                            if (x >= 0 && x < base.length && y >= 0 && y < base[0].length) {
                                r += base[x][y].r;
                                g += base[x][y].g;
                                b += base[x][y].b;
                                a += base[x][y].a;
                                hits++;
                            }
                        }
                    }
                }
                if (hits > 0) {
                    base[ii][jj] = new Color(r / hits, g / hits, b / hits, a / hits);
                }
            }
        }
    }
}
