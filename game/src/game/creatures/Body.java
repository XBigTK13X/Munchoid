package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.arena.Floor;
import sps.draw.Colors;
import sps.util.MathHelper;

public class Body {
    private BodyParts _parts;
    private Creature _owner;
    private float _width;
    private float _height;
    private boolean _flipX;
    private Color _color;
    private static Floor __floor;
    private float _scale;

    private Color _highlight = Color.WHITE;

    private int _health;
    private int _healthMax;

    public Body() {
        this(Colors.randomPleasant());
    }

    public Body(Color color) {
        _parts = new BodyParts(this);
        _color = color;
        _parts = Engineer.designParts(this);
        calculateSize();
    }

    private void calculateSize() {
        _parts.getCore().calculateOrigins();

        for (BodyPart part : _parts.getAll()) {
            if (part.getWidth() + part.getPosition().X > _width) {
                _width = part.getWidth() + part.getPosition().X;
            }
            if (part.getWidth() + part.getPosition().Y > _height) {
                _height = part.getHeight() + part.getPosition().Y;
            }
        }

        recalculateHealth();
    }

    public void draw() {
        _parts.draw();
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
        _scale = scale;
        for (BodyPart part : _parts.getAll()) {
            part.setScale(scale);
        }
    }

    public void restore() {
        for (BodyPart part : _parts.getAll()) {
            part.restore();
        }
        _owner.addHealthOffset(-_owner.getHealthOffset());
        _owner.restoreEnergy();
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

    public BodyParts getParts() {
        return _parts;
    }

    public int getHealth() {
        return MathHelper.clamp(_health + _owner.getHealthOffset(), 0, _healthMax);
    }

    public int getHealthMax() {
        return _healthMax;
    }

    public void recalculateHealth() {
        _health = 0;
        _healthMax = 0;
        for (BodyPart part : _parts.getAll()) {
            if (part.isAlive()) {
                _health += part.getHealth();
            }
            _healthMax += part.getHealthMax();
        }
    }

    public void setFloor(Floor floor) {
        __floor = floor;
    }

    public boolean anyPartOutsideArena(float dX, float dY) {
        return _parts.areAnyOutside(dX, dY, __floor);
    }

    public void setHighlight(Color color) {
        _highlight = color;
    }

    public Color getHighlight() {
        return _highlight;
    }

    public float getPercentHealth() {
        return (getHealth() / (float) getHealthMax());
    }

    public float getScale() {
        return _scale;
    }
}
