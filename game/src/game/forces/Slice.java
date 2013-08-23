package game.forces;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.creatures.Atom;
import game.creatures.BodyPart;
import sps.core.Point2;
import sps.core.RNG;
import sps.particles.ParticleEngine;
import sps.particles.behaviors.SliceBehavior;

import java.awt.*;

public class Slice extends BaseForce {
    private static final int __minLineLength = 5;

    Point2 _start = new Point2(0, 0);
    Point2 _end = new Point2(0, 0);

    Polygon _blade;

    public Slice(int magnitude) {
        super(magnitude, GameConfig.SliceScale);
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        if (_blade.contains(ii, jj)) {
            return false;
        }
        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        _start.reset(0, 0);

        Atom[][] atoms = part.getAtoms();

        boolean vert = RNG.coinFlip();
        int thickness = getScaledMagnitude() / 2;

        if (vert) {
            _start.reset(-thickness, RNG.next(0, atoms[0].length));
            _end.reset(atoms.length + thickness, RNG.next(0, atoms[0].length));
        }
        else {
            _start.reset(RNG.next(0, atoms.length), -thickness);
            _end.reset(RNG.next(0, atoms.length), atoms[0].length + thickness);
        }

        float rotRads = (float) Math.atan2(_end.Y - _start.Y, _end.X - _start.X);
        int[] boundsX = new int[4];
        int[] boundsY = new int[4];

        boundsX[0] = (int) (_start.X + Math.cos(rotRads + Math.PI / 2) * thickness);
        boundsY[0] = (int) (_start.Y + Math.sin(rotRads + Math.PI / 2) * thickness);
        boundsX[1] = (int) (_start.X + Math.cos(rotRads - Math.PI / 2) * thickness);
        boundsY[1] = (int) (_start.Y + Math.sin(rotRads - Math.PI / 2) * thickness);
        boundsX[2] = (int) (_end.X + Math.cos(rotRads - Math.PI / 2) * thickness);
        boundsY[2] = (int) (_end.Y + Math.sin(rotRads - Math.PI / 2) * thickness);
        boundsX[3] = (int) (_end.X + Math.cos(rotRads + Math.PI / 2) * thickness);
        boundsY[3] = (int) (_end.Y + Math.sin(rotRads + Math.PI / 2) * thickness);

        _blade = new

                Polygon(boundsX, boundsY, 4);

    }

    @Override
    public void animate(BodyPart part) {
        ParticleEngine.get().emit(SliceBehavior.getInstance(), part.getGlobalPosition(), Color.WHITE);
    }
}
