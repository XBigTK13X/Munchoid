package game.creatures;

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
            part = new BodyPart(RNG.next(partWidthMin, partWidthMax), RNG.next(partHeightMin, partHeightMax), this);
            _parts.add(part);
        }
        calculateSize();
    }

    private void calculateSize() {
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
            part.draw();
        }
    }

    public BodyPart getRandomPart() {
        return _parts.get(RNG.next(0, _parts.size()));
    }

    public void update() {
        for (int ii = 0; ii < _parts.size(); ii++) {
            if (!_parts.get(ii).isAlive()) {
                _parts.remove(ii);
            }
        }
    }

    public boolean isAlive() {
        return _parts.size() > 0;
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
}
