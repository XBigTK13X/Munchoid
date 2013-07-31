package game.creatures;

import game.arena.Floor;
import game.creatures.style.BodyRules;
import sps.core.RNG;
import sps.util.Bounds;

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
        if (_parts.size() > 1) {
            if (RNG.coinFlip()) {
                _front.add(part);
            }
            else {
                _back.add(part);
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

    public BodyPart getAParent() {
        if (_parts.size() == 0) {
            return null;
        }
        while (true) {
            BodyPart target = _parts.get(RNG.next(0, _parts.size()));
            if (BodyRules.supports(target.getFunction())) {
                return target;
            }
        }
    }

    public void draw() {
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

    public boolean areAnyOutside(float dX, float dY, Floor floor) {
        if (floor == null) {
            return false;
        }
        Bounds b;
        for (BodyPart p : _parts) {
            b = Bounds.fromDimensions(dX + p.getGlobalPosition().X, dY + p.getGlobalPosition().Y, (int) (p.getWidth() * p.getScale()), (int) (p.getHeight() * p.getScale()));
            if (!floor.getBounds().envelopes(b)) {
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
