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
    private Color _partTint;
    private float _scale;
    private PartFunction _function;
    private Body _body;
    private Point2 _position;
    private int _health;
    private int _healthMax = 100;
    private int _rotationDegrees = 0;
    private Point2 _pivot = new Point2(0, 0);

    private BodyPart _parent;
    private Joint _parentConnection;

    private Joints _joints;

    public BodyPart(PartFunction function, int width, int height, Body owner, Design design) {
        this(function, owner, owner.getColor(), new Point2(0, 0));
        _partTint = Color.WHITE;
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
        _body = owner;
        _color = color;
        _position = position;
        _health = _healthMax;
    }

    private void applyStyle() {
        Color[][] atomColors = AtomHelper.getColors(_atoms);
        Outline.single(atomColors, Color.WHITE, GameConfig.BodyPartOutlinePixelThickness);
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
        setPosition(Grid.getPositionRelativeToParent(this));
        _rotationDegrees = Grid.getRotationRelativeToParentInDegrees(this);
        _pivot = Grid.getRotationPivot(this);
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

    public int getFlipMult() {
        return (_body.isFlipX() ? -1 : 1);
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

        _body.recalculateHealth();
        createSprite();
    }

    public void draw() {
        _joints.draw();
        if (GameConfig.DevSkeletonTest) {
            _sprite.setColor(_partTint);
        }
        else {
            _sprite.setColor(_body.getHighlight());
        }
        _sprite.setOrigin(_pivot.X * _scale, _pivot.Y * _scale);
        _sprite.setPosition(getCheapGlobalPosition().X, getCheapGlobalPosition().Y);
        _sprite.setRotation(_rotationDegrees);
        _sprite.setSize(_width * _scale, _height * _scale);
        _sprite.setPosition(getCheapGlobalPosition().X, getCheapGlobalPosition().Y);
        Window.get().schedule(_sprite, DrawDepths.get("BodyPart"));
    }

    private Point2 _expensiveGlobalPosition = new Point2(0, 0);

    public Point2 getExpensiveGlobalPosition() {
        float parentX = 0;
        float parentY = 0;

        //TODO This is really ugly. Should be able to account for Creature pos in the Core
        if (_parent != null) {
            parentX += _parentConnection.getGlobalCenter().X;
            parentY += _parentConnection.getGlobalCenter().Y;
            if (_body.getOwner() != null) {
                parentX -= _body.getOwner().getLocation().X;
                parentY -= _body.getOwner().getLocation().Y;
            }
        }

        _expensiveGlobalPosition.reset(getPosition().X * _scale + parentX, getPosition().Y * _scale + parentY);
        if (_body.getOwner() != null) {
            _expensiveGlobalPosition.reset(_expensiveGlobalPosition.X + _body.getOwner().getLocation().X, _expensiveGlobalPosition.Y + _body.getOwner().getLocation().Y);
        }
        return _expensiveGlobalPosition;
    }

    private Point2 _cheapPositionCache = new Point2(0, 0);
    private Point2 _cheapGlobalPosition = new Point2(0, 0);

    public Point2 getCheapGlobalPosition() {
        _cheapGlobalPosition.reset(_body.getOwner().getLocation().X + _cheapPositionCache.X, _body.getOwner().getLocation().Y + _cheapPositionCache.Y);
        return _cheapGlobalPosition;
    }

    private Point2 _globalCenter = new Point2(0, 0);

    public Point2 getCheapGlobalCenter() {
        _globalCenter = getCheapGlobalPosition().add(getWidth() / 2, getHeight() / 2);
        return _globalCenter;
    }

    public void recalculateCheapPositionCache() {
        _cheapPositionCache.reset(getExpensiveGlobalPosition().X - _body.getOwner().getLocation().X, getExpensiveGlobalPosition().Y - _body.getOwner().getLocation().Y);
    }

    public void setScale(float scale) {
        _scale = scale;
        setPosition(Grid.getPositionRelativeToParent(this));
        recalculateCheapPositionCache();
    }

    public Point2 getPosition() {
        return _position;
    }

    public boolean isAlive() {
        return _isAlive;
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

    public int getRotationDegrees() {
        return _rotationDegrees;
    }

    public Point2 getPivot() {
        return _pivot;
    }

    public Joint getParentConnection() {
        return _parentConnection;
    }

    public void setTint(Color tint) {
        _partTint = tint;
    }
}