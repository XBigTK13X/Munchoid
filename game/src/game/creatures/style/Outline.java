package game.creatures.style;

import com.badlogic.gdx.graphics.Color;
import game.creatures.Atom;
import sps.util.Colors;

public class Outline {
    private static final float __thicknessPercent = 2f;

    //TODO Remove this and instead create a Atom[][] <-> Color Transformer
    public static void complimentary(Atom[][] atoms) {
        int thicknessX = (int) (atoms.length * (__thicknessPercent / 100f));
        int thicknessY = (int) (atoms[0].length * (__thicknessPercent / 100f));

        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[0].length; jj++) {
                if (atoms[ii][jj] != null) {
                    boolean shift = false;
                    for (int mm = 1; mm <= thicknessX; mm++) {
                        if (ii - thicknessX < 0 || jj - thicknessY < 0 || ii + thicknessX > atoms.length || jj + thicknessY > atoms[0].length) {
                            shift = true;
                        }
                        else {
                            for (int kk = -thicknessX; kk < thicknessX; kk++) {
                                for (int ll = -thicknessY; ll < thicknessY; ll++) {
                                    if (kk != 0 || ll != 0) {
                                        //TODO also check if active, so that outlines will change after destruction
                                        if (atoms[ii + kk][jj + ll] == null) {
                                            shift = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (shift) {
                            atoms[ii][jj].setColor(Colors.hueShift(atoms[ii][jj].getColor(), .5f));
                        }
                    }
                }
            }
        }
    }

    public static void complimentary(Color[][] colors) {
        int thicknessX = (int) (colors.length * (__thicknessPercent / 100f));
        int thicknessY = (int) (colors[0].length * (__thicknessPercent / 100f));

        for (int ii = 0; ii < colors.length; ii++) {
            for (int jj = 0; jj < colors[0].length; jj++) {
                if (colors[ii][jj] != null) {
                    boolean shift = false;
                    for (int mm = 1; mm <= thicknessX; mm++) {
                        if (ii - thicknessX < 0 || jj - thicknessY < 0 || ii + thicknessX > colors.length || jj + thicknessY > colors[0].length) {
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
                        if (shift) {
                            colors[ii][jj] = (Colors.hueShift(colors[ii][jj], .5f));
                        }
                    }
                }
            }
        }
    }
}
