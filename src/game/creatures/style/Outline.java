package game.creatures.style;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.draw.Colors;

public class Outline {
    public static enum Mode {
        Naive,
        Fast,
        None
    }

    private interface ColorPicker {
        public Color convert(Color color);
    }

    private static class Compliment implements ColorPicker {
        @Override
        public Color convert(Color color) {
            return Colors.hueShift(color, .5f);
        }
    }

    private static class Single implements ColorPicker {
        private Color _choice;

        public Single(Color choice) {
            _choice = choice;
        }

        @Override
        public Color convert(Color color) {
            return _choice;
        }
    }

    private static final Compliment compliment = new Compliment();

    ////Adapted from http://ostermiller.org/dilate_and_erode.html
    private static void fast(Color[][] colors, ColorPicker picker, int pixelThickness) {
        //See if there's a way to adapt this approach to function like naive
        //In its current form, it doesn't work at all
        int[][] image = new int[colors.length][colors[0].length];
        for (int ii = 0; ii < colors.length; ii++) {
            for (int jj = 0; jj < colors[0].length; jj++) {
                image[ii][jj] = colors[ii][jj] == null ? 0 : 1;
            }
        }

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 1) {
                    image[i][j] = 0;
                }
                else {
                    image[i][j] = image.length + image[i].length;
                    if (i > 0) {
                        image[i][j] = Math.min(image[i][j], image[i - 1][j] + 1);
                    }
                    if (j > 0) {
                        image[i][j] = Math.min(image[i][j], image[i][j - 1] + 1);
                    }
                }
            }
        }
        for (int i = image.length - 1; i >= 0; i--) {
            for (int j = image[i].length - 1; j >= 0; j--) {
                if (i + 1 < image.length) {
                    image[i][j] = Math.min(image[i][j], image[i + 1][j] + 1);
                }
                if (j + 1 < image[i].length) {
                    image[i][j] = Math.min(image[i][j], image[i][j + 1] + 1);
                }
            }
        }

        for (int ii = 0; ii < image.length; ii++) {
            for (int jj = 0; jj < image[0].length; jj++) {
                if (image[ii][jj] <= pixelThickness && image[ii][jj] > 0) {
                    colors[ii][jj] = picker.convert(colors[ii][jj]);
                }
            }
        }
    }

    private static void naive(Color[][] colors, ColorPicker picker, int pixelThickness) {
        int thicknessX = pixelThickness;
        int thicknessY = pixelThickness;
        boolean[][] shifted = new boolean[colors.length][colors[0].length];

        for (int ii = 0; ii < colors.length; ii++) {
            for (int jj = 0; jj < colors[0].length; jj++) {
                if (colors[ii][jj] != null) {
                    boolean shift = false;
                    for (int mm = 1; mm <= thicknessX; mm++) {
                        if (ii - thicknessX <= 0 || jj - thicknessY <= 0 || ii + thicknessX >= colors.length || jj + thicknessY >= colors[0].length) {
                            shift = true;
                        }
                        else {
                            for (int kk = -thicknessX; kk < thicknessX; kk++) {
                                for (int ll = -thicknessY; ll < thicknessY; ll++) {
                                    if (kk != 0 || ll != 0) {
                                        int adjX = ii + kk;
                                        int adjY = jj + ll;
                                        if (adjX >= 0 && adjX < colors.length - 1 && adjY >= 0 && adjY < colors[0].length) {
                                            //TODO also check if active, so that outlines will change after destruction
                                            // This doesn't really work in cases like Vaporize, where the outline
                                            // basically makes the entire part turn white
                                            if (colors[adjX][adjY] == null) {
                                                shift = true;
                                            }
                                        }
                                        else {
                                            shift = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (shift && !shifted[ii][jj]) {
                            colors[ii][jj] = picker.convert(colors[ii][jj]);
                            shifted[ii][jj] = true;
                        }
                    }
                }
            }
        }
    }

    public static void complimentary(Color[][] colors, int pixelThickness) {
        apply(colors, compliment, pixelThickness);
    }

    public static void single(Color[][] colors, Color outline, int pixelThickness) {
        apply(colors, new Single(outline), pixelThickness);
    }

    public static void apply(Color[][] colors, ColorPicker picker, int pixelThickness) {
        switch (GameConfig.OptOutlineMode) {
            case Naive:
                naive(colors, picker, pixelThickness);
                break;
            case Fast:
                fast(colors, picker, pixelThickness);
                break;
            case None:
                return;
        }
    }
}