package game.forces;

import game.GameConfig;
import game.creatures.BodyPart;
import sps.core.RNG;

public class Abrasive extends BaseForce {
    private int _adjustedMagnitude;
    private int wiggleRoom = 10;
    boolean rubX;
    boolean rubBehind;

    public Abrasive(int magnitude) {
        super(magnitude, GameConfig.AbrasiveScale);
        _adjustedMagnitude = getScaledMagnitude();
    }

    @Override
    public boolean forceSpecifics(BodyPart bodyPart, int ii, int jj) {
        _adjustedMagnitude = _adjustedMagnitude + getPartScale(bodyPart) + RNG.next(0, wiggleRoom * 2) - wiggleRoom;
        if (_adjustedMagnitude > getScaledMagnitude() + wiggleRoom * 3
                || _adjustedMagnitude < getScaledMagnitude() - wiggleRoom * 3) {
            _adjustedMagnitude = getScaledMagnitude() + getPartScale(bodyPart) + RNG.next(0, wiggleRoom * 2) - wiggleRoom;
        }
        if ((rubX && jj < _adjustedMagnitude && !rubBehind)
                || (rubX && jj > bodyPart.getAtoms()[0].length - _adjustedMagnitude && rubBehind)
                || (!rubX && ii < _adjustedMagnitude && !rubBehind)
                || (!rubX && ii > bodyPart.getAtoms().length - _adjustedMagnitude && rubBehind)) {
            return false;
        }

        return bodyPart.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart bodyPart) {
        rubX = RNG.coinFlip();
        rubBehind = RNG.coinFlip();
    }
}
