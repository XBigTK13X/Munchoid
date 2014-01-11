package game.stages.common.creatures;

import sps.color.Color;
import sps.core.Logger;
import sps.util.BoundingBox;

public class AtomHelper {
    public static Color[][] getColors(Atom[][] atoms) {
        Color[][] result = new Color[atoms.length][atoms[0].length];
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[ii].length; jj++) {
                if (atoms[ii][jj] != null) {
                    result[ii][jj] = atoms[ii][jj].getColor();
                }
            }
        }
        return result;
    }

    public static void setColors(Atom[][] atoms, Color[][] colors) {
        if (colors.length != atoms.length || colors[0].length != atoms[0].length) {
            Logger.exception(new RuntimeException("Colors were loaded that didn't match the atoms dimensions."));
        }

        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[ii].length; jj++) {
                if (atoms[ii][jj] != null) {
                    atoms[ii][jj].setColor(colors[ii][jj]);
                }
            }
        }
    }

    public static Atom[][] copy(Atom[][] atoms) {
        Atom[][] result = new Atom[atoms.length][atoms[0].length];
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[0].length; jj++) {
                if (atoms[ii][jj] != null) {
                    result[ii][jj] = new Atom(atoms[ii][jj]);
                }
            }
        }
        return result;
    }

    public static void setColor(Atom[][] atoms, Color color) {
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[0].length; jj++) {
                if (atoms[ii][jj] != null) {
                    atoms[ii][jj].setColor(color);
                }
            }
        }
    }

    public static BoundingBox getEdges(Atom[][] atoms) {
        int leftX = 0;
        boolean found = false;
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[0].length; jj++) {
                if (atoms[ii][jj] != null && atoms[ii][jj].isActive()) {
                    leftX = ii;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int rightX = 0;
        found = false;
        for (int ii = atoms.length - 1; ii > 0; ii--) {
            for (int jj = atoms[0].length - 1; jj > 0; jj--) {
                if (atoms[ii][jj] != null && atoms[ii][jj].isActive()) {
                    rightX = ii + 1;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int bottomY = 0;
        found = false;
        for (int ii = 0; ii < atoms[0].length; ii++) {
            for (int jj = 0; jj < atoms.length; jj++) {
                if (atoms[jj][ii] != null && atoms[jj][ii].isActive()) {
                    bottomY = ii;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        int topY = 0;
        found = false;
        for (int ii = atoms[0].length - 1; ii > 0; ii--) {
            for (int jj = atoms.length - 1; jj > 0; jj--) {
                if (atoms[jj][ii] != null && atoms[jj][ii].isActive()) {
                    topY = ii + 1;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        return BoundingBox.fromPoints(leftX, bottomY, rightX, topY);
    }
}
