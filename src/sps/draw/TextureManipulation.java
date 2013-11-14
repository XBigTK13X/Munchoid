package sps.draw;

import com.badlogic.gdx.graphics.Color;

public class TextureManipulation {
    public static void negative(Color[][] base) {
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = new Color(1f - base[ii][jj].r, 1f - base[ii][jj].g, 1f - base[ii][jj].b, 1f - base[ii][jj].a);
            }
        }
    }

    public static void blurNaive(Color[][] base, int blurriness) {
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

    //Based on: http://www.blackpawn.com/texts/blur/
    private static Color[][] blurHorizontal(Color[][] source, int radius, Color[][] result) {
        for (int y = 0; y < source.length; y++) {
            Color total = new Color(Color.BLACK);

            // Process entire window for first pixel
            for (int kx = -radius; kx <= radius && kx >= 0 && kx < source.length; ++kx) {
                total = total.add(source[kx][y]);
            }
            result[0][y] = Colors.divide(total, (radius * 2 + 1));

            // Subsequent pixels just update window total
            for (int x = 1; x < source[0].length; x++) {
                // Subtract pixel leaving window
                int x1 = x + radius;
                int x2 = x - radius - 1;
                if (x2 >= 0 && x2 < source.length) {
                    total.sub(source[x2][y]);
                }
                // Add pixel entering window
                if (x1 >= 0 && x1 < source.length) {
                    total.add(source[x1][y]);
                }

                result[x][y] = Colors.divide(total, (radius * 2 + 1));
            }
        }
        return result;
    }

    public static void transpose(Color[][] base) {
        for (int y = 0; y < base.length; y++) {
            for (int x = y + 1; x < base[0].length; x++) {
                Color tmp = base[x][y];
                base[x][y] = base[y][x];
                base[y][x] = tmp;
            }
        }
    }

    public static Color[][] blurGaussian(Color[][] base, int radius) {
        Color[][] result = new Color[base.length][base[0].length];
        blurHorizontal(base, radius, result);
        transpose(result);
        blurHorizontal(base, radius, result);
        transpose(result);
        return result;
    }
}
