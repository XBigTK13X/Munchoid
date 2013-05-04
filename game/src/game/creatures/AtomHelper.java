package game.creatures;

import com.badlogic.gdx.graphics.Color;
import sps.core.Logger;

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
}
