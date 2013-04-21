package game.creatures;

import sps.core.Point2;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<BodyPart> _parts;
    private Creature _owner;
    private float _width;
    private float _height;

    public Body(Creature owner, int numberOfParts, int partWidthMin, int partHeightMin, int partWidthMax, int partHeightMax) {
        _owner = owner;
        _parts = new ArrayList<BodyPart>();
        BodyPart part;
        for (int ii = 0; ii < numberOfParts; ii++) {
            boolean core = _parts.size() == 0;
            int mult = core ? 2 : 1;
            PartFunction function = core ? PartFunction.Body : PartFunction.nonBody();
            part = new BodyPart(function, RNG.next(mult * partWidthMin, mult * partWidthMax), RNG.next(mult * partHeightMin, mult * partHeightMax), this);
            _parts.add(part);
        }
        calculateSize();
    }

    private void calculateSize() {
        BodyPart body = _parts.get(0);
        body.setPosition(new Point2(0, 0));
        for (int ii = 1; ii < _parts.size(); ii++) {
            BodyPart part = _parts.get(ii);
            switch (part.getFunction()) {
                case Head:
                    _parts.get(ii).setPosition(RNG.point(0, body.getWidth() - part.getWidth(), part.getHeight() / 3, part.getHeight()));
                    break;
                case UpperLimb:
                    _parts.get(ii).setPosition(RNG.point(-part.getWidth(), -part.getWidth() + body.getWidth() / 4, -part.getHeight() / 3, 0));
                    break;
                case LowerLimb:
                    _parts.get(ii).setPosition(RNG.point(0, body.getWidth() - part.getWidth(), -body.getHeight(), -body.getHeight() + body.getHeight() / 5));
                    break;
                case BodyDetail:
                    _parts.get(ii).setPosition(RNG.point(0, body.getWidth() - part.getWidth(), 0, body.getHeight() - part.getHeight()));
                    break;
                case HeadDetail:
                    _parts.get(ii).setPosition(RNG.point(0, body.getWidth() - part.getWidth(), part.getHeight() / 3, part.getHeight()));
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
        for (int ii = 0; ii < _parts.size(); ii++) {
            if (_parts.get(ii).isAlive()) {
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
}
