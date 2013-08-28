package sps.draw;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.core.Point2;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private static final int _particleIterations = 3000;
    private static final int __particleLength = 50;
    private static final int __edgeBias = 12;
    private static final float __outerBlur = .75f;
    private static final float __innerBlur = .88f;


    //Modified from http://www.nolithius.com/game-development/world-generation-breakdown
    public static Color[][] ovalMask(int width, int height) {
        Color[][] mask = new Color[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                mask[ii][jj] = new Color(0, 0, 0, 0);
            }
        }

        for (int iterations = 0; iterations < _particleIterations; iterations++) {
            int sourceX = (RNG.next(width - (__edgeBias * 2)) + __edgeBias);
            int sourceY = (RNG.next(height - (__edgeBias * 2)) + __edgeBias);

            for (int length = 0; length < __particleLength; length++) {
                sourceX += Math.round(RNG.next(2) - 1);
                sourceY += Math.round(RNG.next(2) - 1);

                if (sourceX < 1 || sourceX > width - 2 || sourceY < 1 || sourceY > height - 2) {

                    break;

                }

                List<Point2> hood = getNeighborhood(sourceX, sourceY, width, height);

                for (Point2 neighbor : hood) {
                    if (mask[(int) neighbor.X][(int) neighbor.Y].a < mask[sourceX][sourceY].a) {
                        sourceX = (int) neighbor.X;
                        sourceY = (int) neighbor.Y;
                        break;
                    }
                }

                mask[sourceX][sourceY].a += .05f;
            }
        }

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                float a = mask[ii][jj].a;
                mask[ii][jj] = new Color(a, a, a, a);
            }
        }

        return mask;
    }

    private static List<Point2> getNeighborhood(int x, int y, int width, int height) {
        List<Point2> result = new ArrayList<>();

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a != 0 || b != 0) {
                    if (x + a >= 0 && x + a < width && y + b >= 0 && y + b < height) {
                        result.add(new Point2(x + a, y + b));
                    }
                }
            }
        }

        Collections.shuffle(result);

        return result;
    }

    private static void blurEdges(int width, int height, Color[][] mask) {
        for (int ix = 0; ix < width; ix++) {
            for (int iy = 0; iy < height; iy++) {
                // Multiply the outer edge and the second outer edge by some constants to ensure the world does not touch the edges.
                if (ix == 0 || ix == width - 1 || iy == 0 || iy == height - 1) mask[ix][iy].a *= __outerBlur;
                else if (ix == 1 || ix == width - 2 || iy == 1 || iy == height - 2)
                    mask[ix][iy].a *= __innerBlur;
            }
        }
    }

    public static void negative(Color[][] base) {
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = new Color(1f - base[ii][jj].r, 1f - base[ii][jj].g, 1f - base[ii][jj].b, 1f - base[ii][jj].a);
            }
        }
    }

    public static Color[][] world(int width, int height) {
        Color[][] base = ProcTextures.perlin(width, height, Colors.rgb(0, 255, 255), Colors.rgb(255, 255, 255), 9);

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                int elevation = (int) (255 * base[ii][jj].r);
                Color tile;
                if (elevation < 255 / 2) {
                    tile = Colors.rgb(0, 0, 255);
                }
                else {
                    tile = Colors.rgb(0, 255, 0);
                }
                base[ii][jj] = tile;
            }
        }

        return base;
    }
}
