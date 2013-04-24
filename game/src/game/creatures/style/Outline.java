package game.creatures.style;

import game.creatures.Atom;
import sps.util.Colors;

public class Outline {
    private static final int thickness = 2;

    public static void complimentary(Atom[][] atoms) {
        for (int ii = 0; ii < atoms.length - 1; ii++) {
            for (int jj = 0; jj < atoms[0].length - 1; jj++) {
                if (atoms[ii][jj] != null) {
                    boolean shift = false;
                    for (int mm = 1; mm <= thickness; mm++) {
                        if (ii - thickness < 0 || jj - thickness < 0 || ii + thickness > atoms.length || jj + thickness > atoms[0].length) {
                            shift = true;
                        }
                        else {
                            for (int kk = -thickness; kk < thickness; kk++) {
                                for (int ll = -thickness; ll < thickness; ll++) {
                                    if (kk != 0 || ll != 0) {
                                        //TODO also check if active, so that outlines will change after destruction
                                        if (atoms[ii + kk][jj + ll] != null) {
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
}
