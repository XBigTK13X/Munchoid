package game.forces;

import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Abrasive extends BaseForce {
    private int _magnitude;
    private int _adjustedMagnitude;
    private int wiggleRoom = 10;
    boolean rubX;
    boolean rubBehind;

    public Abrasive() {
        _magnitude = RNG.next(30, 50);
        _adjustedMagnitude = _magnitude;
    }

    @Override
    public Atom forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        _adjustedMagnitude = _adjustedMagnitude + RNG.next(0, wiggleRoom * 2) - wiggleRoom;
        if (_adjustedMagnitude > _magnitude + wiggleRoom * 3
                || _adjustedMagnitude < _magnitude - wiggleRoom * 3) {
            _adjustedMagnitude = _magnitude + RNG.next(0, wiggleRoom * 2) - wiggleRoom;
        }
        if ((rubX && jj < _adjustedMagnitude && !rubBehind)
                || (rubX && jj > bodyPart.getAtoms()[0].length - _adjustedMagnitude && rubBehind)
                || (!rubX && ii < _adjustedMagnitude && !rubBehind)
                || (!rubX && ii > bodyPart.getAtoms().length - _adjustedMagnitude && rubBehind)) {
            return null;
        }

        return bodyPart.getAtoms()[ii][jj];
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        rubX = RNG.coinFlip();
        rubBehind = RNG.coinFlip();
    }
}
