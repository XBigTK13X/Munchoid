package game.creatures;

import game.arena.Floor;
import game.dev.DevConfig;
import sps.core.RNG;
import sps.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class BodyParts {
    private Body _owner;

    private List<BodyPart> _parts;
    private List<BodyPart> _front;
    private List<BodyPart> _back;

    private boolean _hasEye;

    public BodyParts(Body owner) {
        _owner = owner;
        _parts = new ArrayList<>();
        _front = new ArrayList<>();
        _back = new ArrayList<>();
    }

    public void assignDepth(BodyPart part) {
        if (DevConfig.PartSortingEnabled) {
            if (_parts.size() > 1) {
                if (RNG.coinFlip()) {
                    _front.add(part);
                }
                else {
                    _back.add(part);
                }
            }
        }
    }

    public void copy(List<BodyPart> parts) {
        for (BodyPart part : parts) {
            part.setOwner(_owner);
            _parts.add(part);
            if (_parts.size() > 1) {
                assignDepth(part);
            }
        }
    }

    public BodyPart getCore() {
        return _parts.get(0);
    }

    public List<BodyPart> getAll() {
        return _parts;
    }

    public void add(BodyPart part) {
        _parts.add(part);
    }

    public void draw() {
        if (DevConfig.PartSortingEnabled) {
            for (BodyPart part : _back) {
                drawPart(part);
            }
            if (_parts.size() > 0) {
                drawPart(_parts.get(0));
            }
            for (BodyPart part : _front) {
                drawPart(part);
            }
        }
        else {
            if (_parts.size() > 0) {
                for (BodyPart part : _parts) {
                    drawPart(part);
                }
            }
        }
    }

    private void drawPart(BodyPart part) {
        if (part.isAlive()) {
            part.draw();
        }
    }

    public BodyPart getRandom() {
        if (_parts.size() == 0) {
            return null;
        }
        List<Integer> validIndices = new ArrayList<Integer>();
        for (int ii = 0; ii < _parts.size(); ii++) {
            if (_parts.get(ii).isAlive()) {
                validIndices.add(ii);
            }
        }
        return _parts.get(validIndices.get(RNG.next(0, validIndices.size())));
    }

    BoundingBox _partBounds = BoundingBox.empty();

    public boolean areAnyOutside(float dX, float dY, Floor floor) {
        if (floor == null) {
            return false;
        }

        for (BodyPart p : _parts) {
            BoundingBox.fromDimensions(_partBounds, dX + p.getCheapGlobalPosition().X, dY + p.getCheapGlobalPosition().Y, (int) (p.getWidth() * p.getScale()), (int) (p.getHeight() * p.getScale()));
            if (!floor.getBounds().envelopes(_partBounds)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        _parts.clear();
    }

    public boolean anyAlive() {
        for (BodyPart part : _parts) {
            if (part.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEye() {
        return _hasEye;
    }
}
