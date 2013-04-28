package game.creatures;

import com.badlogic.gdx.graphics.Color;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.Colors;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<BodyPart> _parts;
    private Creature _owner;
    private float _width;
    private float _height;
    private boolean _flipX;
    private Color _color;

    public Body(Creature owner, int numberOfParts, int partWidthMin, int partHeightMin, int partWidthMax, int partHeightMax) {
        _owner = owner;
        _parts = new ArrayList<BodyPart>();
        BodyPart part;
        _color = Colors.randomPleasant();
        //Logger.info("=== New Creature");
        for (int ii = 0; ii < numberOfParts; ii++) {
            boolean core = _parts.size() == 0;
            int mult = core ? 2 : 1;
            PartFunction function = core ? PartFunction.Body : PartFunction.nonBody();
            //Logger.info("--> " + function);
            part = new BodyPart(function, RNG.next(mult * partWidthMin, mult * partWidthMax), RNG.next(mult * partHeightMin, mult * partHeightMax), this);
            _parts.add(part);
        }
        calculateSize();
    }

    private void calculateSize() {
        BodyPart body = _parts.get(0);
        body.setPosition(new Point2(0, 0));
        int bh = body.getHeight();
        int bw = body.getWidth();
        for (int ii = 1; ii < _parts.size(); ii++) {
            BodyPart part = _parts.get(ii);
            int pw = part.getWidth();
            int ph = part.getHeight();
            int wd = bw - pw;
            if (wd < 0) {
                wd = 0;
            }
            int hd = bh - ph;
            if (hd < 0) {
                hd = 0;
            }

            switch (part.getFunction()) {
                case Head:
                    _parts.get(ii).setPosition(RNG.point(0, bw, bh / 2, bh / 2 + ph / 2));
                    break;
                case UpperLimb:
                    _parts.get(ii).setPosition(RNG.point(bw - pw / 3, bw + pw / 4, bh / 2 - ph / 3, bh / 2 + ph / 3));
                    break;
                case LowerLimb:
                    _parts.get(ii).setPosition(RNG.point(0, wd, -ph, -ph + bh / 5));
                    break;
                case BodyDetail:
                    _parts.get(ii).setPosition(RNG.point(0, wd, 0, hd));
                    break;
                case HeadDetail:
                    _parts.get(ii).setPosition(RNG.point(0, wd, ph / 3, ph));
                    break;
            }
        }

        for (BodyPart part : _parts) {
            if (part.getWidth() + part.getPosition().X > _width) {
                _width = part.getWidth() + part.getPosition().X;
            }
            if (part.getWidth() + part.getPosition().Y > _height) {
                _height = part.getHeight() + part.getPosition().Y;
            }
        }
    }

    public void draw() {
        for (BodyPart part : _parts) {
            if (part.isAlive()) {
                part.draw();
            }
        }
    }

    public BodyPart getRandomPart() {
        List<Integer> validIndices = new ArrayList<Integer>();
        for (int ii = 0; ii < _parts.size(); ii++) {
            if (_parts.get(ii).isAlive()) {
                validIndices.add(ii);
            }
        }
        return _parts.get(validIndices.get(RNG.next(0, validIndices.size())));
    }

    public void update() {

    }

    public boolean isAlive() {
        for (BodyPart part : _parts) {
            if (part.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public Creature getOwner() {
        return _owner;
    }

    public float getWidth() {
        return _width;
    }

    public boolean isFlipX() {
        return _flipX;
    }

    public float getHeight() {
        return _height;
    }

    public void setScale(float scale) {
        for (BodyPart part : _parts) {
            part.setScale(scale);
        }
    }

    public void restore() {
        for (BodyPart part : _parts) {
            part.restore();
        }
    }

    public void flipX(boolean faceLeft) {
        _flipX = faceLeft;
    }

    public void kill() {
        _parts.clear();
        _owner.setInactive();
    }

    public Color getColor() {
        return _color;
    }
}
