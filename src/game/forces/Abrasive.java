package game.forces;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import game.GameConfig;
import game.creatures.AtomHelper;
import game.creatures.BodyPart;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.RNG;
import sps.particles.ParticleWrapper;
import sps.util.BoundingBox;

public class Abrasive extends BaseForce {
    private static enum Side {
        Top(-90, 1, 1),
        Bottom(90, -1, -1),
        Left(0, 1, 1),
        Right(180, -1, -1);

        public int Degrees;
        public Point2 Root;

        private Side(int degrees, int x, int y) {
            Degrees = degrees;
            Root = new Point2(x, y);
        }
    }

    private int _adjustedMagnitude;
    private static final float __partPercent = .15f;
    private static final float __wigglePercent = .05f;
    private Side _side;
    private BoundingBox _edges;

    public Abrasive(int magnitude) {
        super(magnitude, GameConfig.AbrasiveScale);
        _adjustedMagnitude = getScaledMagnitude();
    }

    @Override
    public boolean forceSpecifics(BodyPart part, int ii, int jj) {
        //Jitters the force to make the application appear rough
        int impact = RNG.next((int) (_adjustedMagnitude - (_adjustedMagnitude * __wigglePercent)), (int) (_adjustedMagnitude + (_adjustedMagnitude * __wigglePercent)));

        //If an atom is within the jittered edge bounds, delete it
        boolean inRange = false;
        switch (_side) {
            case Top:
                inRange = jj > _edges.Y2 - impact;
                break;
            case Right:
                inRange = ii > _edges.X2 - impact;
                break;
            case Bottom:
                inRange = jj < impact + _edges.Y;
                break;
            case Left:
                inRange = ii < impact + _edges.X;
                break;
        }

        if (inRange) {
            return false;
        }

        return part.getAtoms()[ii][jj].isActive();
    }

    @Override
    public void prepareCalculations(BodyPart part) {
        _side = RNG.pick(Side.values());
        _edges = AtomHelper.getEdges(part.getAtoms());

        _adjustedMagnitude += getPartScale(part) * __partPercent;
    }

    @Override
    public void animate(BodyPart part) {
        ParticleEffect effect = ParticleWrapper.get().emit("abrasive", part.getCheapGlobalPosition());

        //TODO When an edge is partially destroyed the effect still displays on the original edge
        Point2 dimensions = part.calculateRotatedDimensions();
        int w = (int) (part.getScale() * (dimensions.X / 2));
        int h = (int) (part.getScale() * (dimensions.Y / 2));
        String log = "Side: " + _side + ", WxH: " + w + " x " + h;
        Point2 pos = part.getCheapGlobalCenter().add(_side.Root.X * w, h * _side.Root.Y);
        effect.setPosition(pos.X, pos.Y);

        Logger.info(log + ", Pos: " + pos);

        ParticleWrapper.rotate(effect, _side.Degrees + part.getRotationDegrees());

        effect.start();
    }
}