package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;

public abstract class Force {
    public void apply(BodyPart bodyPart) {
        Atom[][] atoms = bodyPart.getAtoms();
        prepareCalculations(bodyPart);
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[ii].length; jj++) {
                if (atoms[ii][jj] != null) {
                    atoms[ii][jj] = forceSpecifics(bodyPart, ii, jj);
                }
            }
        }
        bodyPart.setAtoms(atoms);
    }

    public abstract Atom forceSpecifics(BodyPart bodyPart, int ii, int jj);

    public void prepareCalculations(BodyPart bodyPart) {
    }
}
