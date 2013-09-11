package game.forces;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import game.GameConfig;
import game.creatures.AtomHelper;
import game.creatures.BodyPart;
import sps.core.RNG;
import sps.particles.ParticleWrapper;
import sps.util.BoundingBox;

public class Abrasive extends BaseForce {
    private int _adjustedMagnitude;
    private int wiggleRoom = 10;
    private static final int __wiggleImpact = 3;
    boolean _rubX;
    boolean _rubBehind;
    private BoundingBox _edges;

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
        if ((_rubX && jj < _adjustedMagnitude + _edges.Y && !_rubBehind)
                || (_rubX && jj > part.getAtoms()[0].length - _adjustedMagnitude - _edges.Y2 && _rubBehind)
                || (!_rubX && ii < _adjustedMagnitude + _edges.X && !_rubBehind)
                || (!_rubX && ii > part.getAtoms().length - _adjustedMagnitude - _edges.X2 && _rubBehind)) {
            return false;
        }

        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        _rubX = RNG.coinFlip();
        _rubBehind = RNG.coinFlip();
        _edges = AtomHelper.getEdges(part.getAtoms());
    }

    @Override
    public void animate(BodyPart part) {
        ParticleEffect effect = ParticleWrapper.get().emit("abrasive", part.getGlobalPosition());
        //TODO Rewrite forceSpecs and copy the rotation from there
        int degrees = 0;
        for (int i = 0; i < effect.getEmitters().size; i++) {
            effect.getEmitters().get(i).getAngle().setLow(degrees);
            effect.getEmitters().get(i).getAngle().setHigh(degrees);
        }
        effect.start();
    }
}
