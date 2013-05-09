package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.creatures.style.BodyRules;
import sps.core.Logger;
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
        //1) Pick a part with no dependencies
        //2) Pick parts that either have no deps, or the first piece picked
        //3) Continue choosing parts until limit reached
        //Logger.info("=== New Creature");
        for (int ii = 0; ii < numberOfParts; ii++) {
            //TODO replace core = size x 2 functionality
            //Logger.info("--> " + function);
            part = new BodyPart(BodyRules.getSupported(_parts), RNG.next(partWidthMin, partWidthMax), RNG.next(partHeightMin, partHeightMax), this);
            _parts.add(part);
        }
        calculateSize();
    }

    public Body(List<BodyPart> parts, Color color) {
        _parts = new ArrayList<BodyPart>();
        _color = color;

        for (BodyPart part : parts) {
            _parts.add(new BodyPart(part, this, color));
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

            //TODO Move this into function
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
                case Body:
                    _parts.get(ii).setPosition(new Point2(0, 0));
                    break;
                default:
                    Logger.info("Case not handled while positioning a body part: " + part.getFunction());
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

    public Creature getOwner() {
        return _owner;
    }

    public void setOwner(Creature owner) {
        _owner = owner;
    }

    public List<BodyPart> getParts() {
        return _parts;
    }
}
