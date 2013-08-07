package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.part.Design;
import game.creatures.part.Designs;
import game.creatures.style.Grid;
import game.creatures.style.Outline;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.display.Window;
import sps.draw.SpriteMaker;

public class BodyPart {
    private static final float __scaleBase = 1f;
    private static final float _percentRequiredToLive = .15f;

    private Sprite _sprite;
    private Atom[][] _atoms;
    private int _width;
    private int _height;
    private boolean _isAlive = true;
    private Color _color;
    private float _scale;
    private PartFunction _function;
    private Body _owner;
    private Point2 _position;
    private int _health;
    private int _healthMax = 100;
    private int _rotationDegrees = 0;

    private BodyPart _parent;
    private Joint _parentConnection;

    private Joints _joints;

    public BodyPart(PartFunction function, int width, int height, Body owner, Design design) {
        this(function, owner, owner.getColor(), new Point2(0, 0));

        int[][] designResult = design.create(width, height);

        _atoms = Designs.toAtoms(designResult, _color);
        _width = _atoms.length;
        _height = _atoms[0].length;

        applyStyle();
    }

    public BodyPart(BodyPart source, Body owner, Color color) {
        this(source.getFunction(), owner, color, source.getPosition());
        _atoms = AtomHelper.copy(source.getAtoms());
        _width = source.getWidth();
        _height = source.getHeight();
        Color[][] textureBase = Designs.getTexture(_width, _height, _color);
        for (int ii = 0; ii < _atoms.length; ii++) {
            for (int jj = 0; jj < _atoms[0].length; jj++) {
                if (_atoms[ii][jj] != null) {
                    _atoms[ii][jj].setColor(textureBase[ii][jj]);
                }
            }
        }
        applyStyle();
    }

    private BodyPart(PartFunction function, Body owner, Color color, Point2 position) {
        _scale = __scaleBase;
        _function = function;
        _owner = owner;
        _color = color;
        _position = position;
        _health = _healthMax;
    }

    private void applyStyle() {
        Color[][] atomColors = AtomHelper.getColors(_atoms);
        Outline.single(atomColors, Color.WHITE);
        AtomHelper.setColors(_atoms, atomColors);
        _width = _atoms.length;
        _height = _atoms[0].length;
        _joints = new Joints();

        //TODO Fix this
        boolean locAttachCreated = false;
        for (Integer jointLoc : PartFunction.jointLocations(_function)) {
            if (jointLoc == _function.LocAttach) {
                locAttachCreated = true;
            }
            Joint j = new Joint(jointLoc, this);
            _joints.add(j);
        }
        if (!locAttachCreated && _function.LocAttach != 0) {
            Joint j = new Joint(_function.LocAttach, this);
            _joints.add(j);
        }

        createSprite();
    }

    public void setPosition(Point2 position) {
        _position = position;
    }

    public void calculateOrigins() {
        setPosition(Grid.getOrigin(this));
        if (_joints != null && _joints.getAll().size() > 0) {
            for (Joint joint : _joints.getAll()) {
                if (joint.getChild() != null) {
                    joint.getChild().calculateOrigins();
                }
            }
        }
    }

    private void createSprite() {
        if (_sprite != null) {
            _sprite.getTexture().dispose();
        }
        _sprite = SpriteMaker.get().fromAtoms(_atoms);
    }

    public Joints getJoints() {
        return _joints;
    }

    public Atom[][] getAtoms() {
        return _atoms;
    }

    public void setAtoms(Atom[][] atoms) {
        _atoms = atoms;
        int maxActive = 0;
        int currentActive = 0;
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (atoms[ii][jj] != null) {
                    maxActive++;
                    if (atoms[ii][jj].isActive()) {
                        currentActive++;
                    }
                }
            }
        }
        float percentActive = currentActive / (float) maxActive;
        if (percentActive <= _percentRequiredToLive) {
            _isAlive = false;
        }
        if (_scale <= GameConfig.MinScaleDeath || _scale >= GameConfig.MaxScaleDeath) {
            _isAlive = false;
        }
        int atomHealth = (int) ((percentActive - _percentRequiredToLive) * _healthMax);

        float scaleDelta = 1;
        float scaleDeltaThreshold = 1;
        if (_scale > __scaleBase) {
            scaleDelta = GameConfig.MaxScaleDeath - _scale;
            scaleDeltaThreshold = GameConfig.MaxScaleDeath - __scaleBase;
        }
        else if (_scale < __scaleBase) {
            scaleDelta = _scale - GameConfig.MinScaleDeath;
            scaleDeltaThreshold = __scaleBase - GameConfig.MinScaleDeath;
        }
        int scaleHealth = (int) ((scaleDelta / scaleDeltaThreshold) * _healthMax);

        _health = (atomHealth + scaleHealth) / 2;

        _owner.recalculateHealth();
        createSprite();
    }

    public void draw() {
        float dirScale = _scale * (_owner.isFlipX() ? -1 : 1);
        _sprite.setRotation(_rotationDegrees);
        _joints.draw();
        Window.get().draw(_sprite, getGlobalPosition(), DrawDepths.get("Atom"), _owner.getHighlight(), _width * dirScale, _height * _scale);
    }

    public Point2 getGlobalPosition() {
        float dirScale = _scale * (_owner.isFlipX() ? -1 : 1);
        float xOffset = _owner.isFlipX() ? _owner.getWidth() : 0;
        float parentX = 0;
        float parentY = 0;

        //TODO This is really ugly. Should be able to account for Creature pos in the Core
        if (_parent != null) {
            parentX += _parentConnection.getGlobalCenter().X;
            parentY += _parentConnection.getGlobalCenter().Y;
            if (_owner.getOwner() != null) {
                parentX -= _owner.getOwner().getLocation().X;
                parentY -= _owner.getOwner().getLocation().Y;
            }
        }

        Point2 scaledLoc = new Point2((getPosition().X + parentX) * dirScale + xOffset, (getPosition().Y + parentY) * _scale);
        if (_owner.getOwner() != null) {
            scaledLoc = scaledLoc.addRaw(_owner.getOwner().getLocation());
        }
        return scaledLoc;
    }

    public Point2 getPosition() {
        return _position;
    }

    public boolean isAlive() {
        return _isAlive;
    }

    public void setScale(float scale) {
        _scale = scale;
    }

    public float getScale() {
        return _scale;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public void restore() {
        _scale = 1;
        _health = _healthMax;
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (_atoms[ii][jj] != null) {
                    _atoms[ii][jj].setActive(true);
                }
            }
        }
        _isAlive = true;
        createSprite();
    }

    public PartFunction getFunction() {
        return _function;
    }

    public BodyPart getParent() {
        return _parent;
    }

    public void setParent(BodyPart parent, Joint parentConnection) {
        parentConnection.setChild(this);
        _parent = parent;
        _parentConnection = parentConnection;
    }

    public int getHealth() {
        return _health;
    }

    public int getHealthMax() {
        return _healthMax;
    }

    public void setRotation(int degrees) {
        _rotationDegrees = degrees;
    }

    public Joint getParentConnection() {
        return _parentConnection;
    }

    public Color getColor() {
        return _color;
    }
}