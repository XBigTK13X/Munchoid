package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.creatures.style.BodyRules;
import game.skeleton.Skeleton;
import sps.core.RNG;
import sps.entities.HitTest;
import sps.graphics.Renderer;
import sps.ui.Bounds;
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

    private List<BodyPart> _front;
    private List<BodyPart> _back;

    private Skeleton _skeleton;

    private int _health;
    private int _healthMax;

    public Body(int numberOfParts) {
        this(numberOfParts, (int) GameConfig.MaxBodyPartSize.Y, (int) GameConfig.MinBodyPartSize.Y, (int) GameConfig.MaxBodyPartSize.X, (int) GameConfig.MinBodyPartSize.X);
    }

    public Body(int numberOfParts, int partWidthMin, int partHeightMin, int partWidthMax, int partHeightMax) {
        this(Colors.randomPleasant());

        for (int ii = 0; ii < numberOfParts; ii++) {
            BodyPart parent = BodyRules.getParent(_parts);
            PartFunction function = BodyRules.getChildFunction(parent);
            BodyPart part = new BodyPart(function, RNG.next((int) (partWidthMin * function.Mult), (int) (partWidthMax * function.Mult)), RNG.next((int) (partHeightMin * function.Mult), (int) (partHeightMax * function.Mult)), this);
            if (parent != null) {
                parent.addChild(part);
                assignDepth(part);
            }
            _parts.add(part);
        }
        calculateSize();
    }

    public Body(List<BodyPart> parts, Color color) {
        this(color);

        for (BodyPart part : parts) {
            BodyPart copiedPart = new BodyPart(part, this, _color);
            _parts.add(copiedPart);
            if (_parts.size() > 1) {
                assignDepth(copiedPart);
            }
        }

        calculateSize();
    }

    private Body(Color color) {
        _parts = new ArrayList<BodyPart>();
        _front = new ArrayList<BodyPart>();
        _back = new ArrayList<BodyPart>();
        _color = color;
    }

    private void assignDepth(BodyPart part) {
        if (_parts.size() > 1) {
            if (RNG.coinFlip()) {
                _front.add(part);
            }
            else {
                _back.add(part);
            }
        }
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

        _skeleton = new Skeleton(_parts.get(0));
        recalculateHealth();
    }

    private void drawPart(BodyPart part) {
        if (part.isAlive()) {
            part.draw();
        }
        if (GameConfig.DevDrawSkeleton) {
            _skeleton.draw();
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
        if (GameConfig.DevDrawSkeleton) {
            _skeleton.update();
        }
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
        recalculateHealth();
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

    public int getHealth() {
        return _health;
    }

    public int getHealthMax() {
        return _healthMax;
    }

    public void recalculateHealth() {
        _health = 0;
        _healthMax = 0;
        for (BodyPart part : _parts) {
            if (part.isAlive()) {
                _health += part.getHealth();
            }
            _healthMax += part.getHealthMax();
        }
    }


    public boolean anyPartOffScreen(float dX, float dY) {
        Bounds b;
        Bounds screen = new Bounds(0, 0, Renderer.get().VirtualWidth, Renderer.get().VirtualHeight);
        for (BodyPart p : _parts) {
            b = new Bounds(dX + p.getGlobalPosition().X, dY + p.getGlobalPosition().Y, p.getWidth(), p.getHeight());
            if (!HitTest.inBox(b.X, b.Y, screen) || !HitTest.inBox(b.X, b.Y2, screen) || !HitTest.inBox(b.X2, b.Y, screen) || !HitTest.inBox(b.X2, b.Y2, screen)) {
                return true;
            }
        }
        return false;
    }
}
