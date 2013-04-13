package game.forces;

import game.creatures.Atom;
import sps.core.RNG;

public class AbrasiveForce extends Force {
    private int _magnitude;
    private int _adjustedMagnitude;
    private int wiggleRoom = 10;
    boolean rubX;
    boolean rubBehind;

    public AbrasiveForce() {
        _magnitude = RNG.next(30, 50);
        _adjustedMagnitude = _magnitude;
    }

    @Override
    public Atom forceSpecifics(Atom[][] atoms, int ii, int jj) {
        _adjustedMagnitude = _adjustedMagnitude + RNG.next(0, wiggleRoom * 2) - wiggleRoom;
        if (_adjustedMagnitude > _magnitude + wiggleRoom * 3
                || _adjustedMagnitude < _magnitude - wiggleRoom * 3) {
            _adjustedMagnitude = _magnitude + RNG.next(0, wiggleRoom * 2) - wiggleRoom;
        }
        if ((rubX && jj < _adjustedMagnitude && !rubBehind)
                || (rubX && jj > atoms[0].length - _adjustedMagnitude && rubBehind)
                || (!rubX && ii < _adjustedMagnitude && !rubBehind)
                || (!rubX && ii > atoms.length - _adjustedMagnitude && rubBehind)) {
            return null;
        }

        return atoms[ii][jj];
    }

    @Override
    public void prepareCalculations(Atom[][] atoms) {
        rubX = RNG.coinFlip();
        rubBehind = RNG.coinFlip();
    }
}
