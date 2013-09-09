package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;

public abstract class BaseForce {
    protected static int NoScale = 1;

    private int _magnitude;
    private int _scale;

    public BaseForce(int magnitude, int scale) {
        _magnitude = magnitude;
        _scale = scale;
    }

    public void apply(BodyPart part) {
        Atom[][] atoms = part.getAtoms();
        prepareCalculations(part);
        animate(part);
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[ii].length; jj++) {
                if (atoms[ii][jj] != null) {
                    boolean active = forceSpecifics(part, ii, jj);
                    atoms[ii][jj].setActive(active);
                }
            }
        }
        part.setAtoms(atoms);
    }

    public abstract boolean forceSpecifics(BodyPart part, int ii, int jj);

    public void prepareCalculations(BodyPart part) {
    }

    public int getMagnitude() {
        return _magnitude;
    }

    public int getScale() {
        return _scale;
    }

    public int getScaledMagnitude() {
        return _magnitude * _scale;
    }

    public int getPartScale(BodyPart part) {
        return (int) ((part.getWidth() + part.getHeight()) / 2 * (getScale() / 100f));
    }

    public abstract void animate(BodyPart part);
}
