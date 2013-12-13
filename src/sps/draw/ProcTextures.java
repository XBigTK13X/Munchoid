package sps.draw;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.color.Colors;
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
        Color target = new Color(color);
        Color[][] base = new Color[width][height];
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = target;
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

    public static Color[][] radial(int width, int height, Color start, Color end, int steps, Point2 center, boolean fixed) {
        boolean colorDebug = false;
        if (colorDebug) {
            start = new Color(0xCD853FFF);
            end = new Color(0x20B2AAFF);
            center.reset(0, 0);
        }

        Color[][] result = new Color[width][height];

        int maxDist = (int) (Math.sqrt(height * height + width * width));

        Color[] g = null;

        for (int ii = 0; ii < result.length; ii++) {
            for (int jj = 0; jj < result[0].length; jj++) {
                int dist = (int) HitTest.getDistance(ii, jj, center.X, center.Y);
                float percentDist = MathHelper.valueToPercent(0, maxDist, dist);
                if (fixed) {
                    if (g == null) {
                        g = Colors.gradient(start, end, steps);
                    }
                    int scaledDist = (int) ((percentDist / 100f) * steps);
                    result[ii][jj] = g[scaledDist];
                }
                else {
                    result[ii][jj] = Colors.interpolate(percentDist, start, end);
                }
            }
        }

        /*
        if(GameConfig.OptDitheringEnabled){
                  for(int i=0; i<height; i++){
                       for(int j=0; j<width; j++){
                           int ci = i*w+j;               // current buffer index
                           Color cc = sb[ci];              // current color
                           var rc = (cc<128?0:255);      // real (rounded) color
                           var err = cc-rc;              // error amount
                           sb[ci] = rc;                  // saving real color
                           if(j+1<w) sb[ci  +1] += (err*7)>>4;  // if right neighbour exists
                            if(i+1==h) continue;   // if we are in the last line
                            if(j  >0) sb[ci+w-1] += (err*3)>>4;  // bottom left neighbour
                                   sb[ci+w  ] += (err*5)>>4;  // bottom neighbour
                           if(j+1<w) sb[ci+w+1] += (err*1)>>4;  // bottom right neighbour
                        }
               }
               */

        return result;
    }

    public static Color[][] fixedRadial(int width, int height, Color start, Color end, int steps) {
        return radial(width, height, start, end, steps, RNG.point(0, width, 0, height), true);
    }

    public static Color[][] smoothRadial(int width, int height, Color start, Color end) {
        return smoothRadial(width, height, start, end, RNG.point(0, width, 0, height));
    }

    public static Color[][] smoothRadial(int width, int height, Color start, Color end, Point2 center) {
        return radial(width, height, start, end, 0, center, false);
    }
}
