package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.Logger;

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
        animate(part);
        prepareCalculations(part);
        boolean anyChanged = false;
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[ii].length; jj++) {
                if (atoms[ii][jj] != null) {
                    boolean active = forceSpecifics(part, ii, jj);
                    if (active != atoms[ii][jj].isActive()) {
                        anyChanged = true;
                    }
                    atoms[ii][jj].setActive(active);
                }
            }
        }
        if (!anyChanged) {
            apply(part);
            Logger.info("No atoms were changed. The force was: " + getClass());
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
