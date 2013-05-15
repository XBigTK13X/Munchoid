package game.forces;

import game.GameConfig;
import game.creatures.AtomHelper;
import game.creatures.BodyPart;
import sps.core.RNG;
import sps.ui.Bounds;

public class Abrasive extends BaseForce {
    private int _adjustedMagnitude;
    private int wiggleRoom = 10;
    private static final int __wiggleImpact = 3;
    boolean rubX;
    boolean rubBehind;
    private Bounds _edges;

    public Abrasive(int magnitude) {
        super(magnitude, GameConfig.AbrasiveScale);
        _adjustedMagnitude = getScaledMagnitude();
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        //TODO Explain what this code should be doing
        _adjustedMagnitude = _adjustedMagnitude + getPartScale(part) + RNG.next(0, wiggleRoom * (__wiggleImpact - 1)) - wiggleRoom;
        if (_adjustedMagnitude > getScaledMagnitude() + wiggleRoom * __wiggleImpact
                || _adjustedMagnitude < getScaledMagnitude() - wiggleRoom * __wiggleImpact) {
            _adjustedMagnitude = getScaledMagnitude() + getPartScale(part) + RNG.next(0, wiggleRoom * (__wiggleImpact - 1)) - wiggleRoom;
        }
        if ((rubX && jj < _adjustedMagnitude + _edges.Y && !rubBehind)
                || (rubX && jj > part.getAtoms()[0].length - _adjustedMagnitude - _edges.Y2 && rubBehind)
                || (!rubX && ii < _adjustedMagnitude + _edges.X && !rubBehind)
                || (!rubX && ii > part.getAtoms().length - _adjustedMagnitude - _edges.X2 && rubBehind)) {
            return false;
        }

        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        rubX = RNG.coinFlip();
        rubBehind = RNG.coinFlip();
        _edges = AtomHelper.getEdges(part.getAtoms());
    }

    @Override
    public void animate(BodyPart part) {
    }
}
