package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.creatures.style.BodyRules;
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

    public Body(int numberOfParts, int partWidthMin, int partHeightMin, int partWidthMax, int partHeightMax) {
        _parts = new ArrayList<BodyPart>();
        BodyPart part;
        _color = Colors.randomPleasant();

        for (int ii = 0; ii < numberOfParts; ii++) {
            BodyPart parent = BodyRules.getParent(_parts);
            PartFunction function = BodyRules.getChildFunction(parent);
            part = new BodyPart(function, RNG.next((int) (partWidthMin * function.Mult), (int) (partWidthMax * function.Mult)), RNG.next((int) (partHeightMin * function.Mult), (int) (partHeightMax * function.Mult)), this);
            if (parent != null) {
                parent.addChild(part);
            }
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
        //This needs to be the body!
        _parts.get(0).calculateOrigins();

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
