package game.creatures;

import game.GameConfig;
import game.arena.Floor;
import sps.core.RNG;
import sps.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class BodyParts {
    private Body _owner;

    private List<BodyPart> _parts;
    private List<BodyPart> _front;
    private List<BodyPart> _back;

    public BodyParts(Body owner) {
        _owner = owner;
        _parts = new ArrayList<BodyPart>();
        _front = new ArrayList<BodyPart>();
        _back = new ArrayList<BodyPart>();
    }

    public void assignDepth(BodyPart part) {
        if (GameConfig.DevPartSortingEnabled) {
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
            BodyPart copiedPart = new BodyPart(part, _owner, _owner.getColor());
            _parts.add(copiedPart);
            if (_parts.size() > 1) {
                assignDepth(copiedPart);
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
        if (GameConfig.DevPartSortingEnabled) {
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
}
