package game.creatures.style;

import com.badlogic.gdx.graphics.Color;
import sps.core.Logger;
import sps.util.Colors;

public class Outline {
    private static final float __thicknessPercent = 2f;

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

    public static void complimentary(Color[][] colors) {
        apply(colors, compliment);
    }

    public static void single(Color[][] colors, Color outline) {
        apply(colors, new Single(outline));
    }

    public static void apply(Color[][] colors, ColorPicker picker) {
        int thicknessX = (int) (colors.length * (__thicknessPercent / 100f) + 1);
        int thicknessY = (int) (colors[0].length * (__thicknessPercent / 100f) + 1);
        boolean[][] shifted = new boolean[colors.length][colors[0].length];

        Logger.info("tX: " + thicknessX + ", tY: " + thicknessY + ", cW: " + colors.length + ", cH: " + colors[0].length);

        for (int ii = 0; ii < colors.length; ii++) {
            for (int jj = 0; jj < colors[0].length; jj++) {
                if (colors[ii][jj] != null) {
                    boolean shift = false;
                    for (int mm = 1; mm <= thicknessX; mm++) {
                        if (ii - thicknessX < 0 || jj - thicknessY < 0 || ii + thicknessX > colors.length - 1 || jj + thicknessY > colors[0].length - 1) {
                            shift = true;
                        }
                        else {
                            for (int kk = -thicknessX; kk < thicknessX; kk++) {
                                for (int ll = -thicknessY; ll < thicknessY; ll++) {
                                    if (kk != 0 || ll != 0) {
                                        //TODO also check if active, so that outlines will change after destruction
                                        if (colors[ii + kk][jj + ll] == null) {
                                            shift = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (shift && !shifted[ii][jj]) {
                            colors[ii][jj] = picker.convert(colors[ii][jj]);
                            Logger.info("SHIFT: " + ii + " , " + jj);
                            shifted[ii][jj] = true;
                        }
                    }
                }
            }
        }
    }
}
