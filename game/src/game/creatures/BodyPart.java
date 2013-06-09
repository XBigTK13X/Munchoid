package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.part.Designs;
import game.creatures.style.BodyRules;
import game.creatures.style.Outline;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.SpriteMaker;

import java.util.ArrayList;
import java.util.List;

public class BodyPart {
    private Sprite _sprite;
    private Atom[][] _atoms;
    private int _width;
    private int _height;
    private float _percentRequiredToLive = .15f;
    private boolean _isAlive = true;
    private Color _color;
    private float _scale;
    private PartFunction _function;
    private Body _owner;
    private Point2 _position;
    private BodyPart _parent;
    private List<BodyPart> _children;
    private int _health;
    private int _healthMax = 100;

    public BodyPart(PartFunction function, int width, int height, Body owner) {
        this(function, owner, owner.getColor(), new Point2(0, 0));
        _children = new ArrayList<BodyPart>();

        int[][] design = Designs.get(_function).create(width, height);
        _atoms = Designs.toAtoms(design, _color);
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
        _scale = 1f;
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
        createSprite();
    }

    private void createSprite() {
        if (_sprite != null) {
            _sprite.getTexture().dispose();
        }
        _sprite = SpriteMaker.get().fromAtoms(_atoms);
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
        _health = (int) ((percentActive - _percentRequiredToLive) * _healthMax);
        _owner.recalculateHealth();
        createSprite();
    }

    public void draw() {
        float dirScale = _scale * (_owner.isFlipX() ? -1 : 1);
        Renderer.get().draw(_sprite, getGlobalPosition(), DrawDepths.get("Atom"), Color.WHITE, _width * dirScale, _height * _scale);
    }

    public Point2 getGlobalPosition() {
        float dirScale = _scale * (_owner.isFlipX() ? -1 : 1);
        float xOffset = (_parent != null && _owner.isFlipX()) ? _parent.getWidth() : 0;
        xOffset = (_parent == null && _owner.isFlipX()) ? getWidth() : xOffset;
        float parentX = (_parent != null ? _parent.getPosition().X : 0);
        float parentY = (_parent != null ? _parent.getPosition().Y : 0);

        Point2 scaledLoc = new Point2((getPosition().X + parentX) * dirScale + xOffset, (getPosition().Y + parentY) * _scale);
        scaledLoc = scaledLoc.addRaw(_owner.getOwner().getLocation());
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

    public void setPosition(Point2 position) {
        _position = position;
    }

    public PartFunction getFunction() {
        return _function;
    }

    public void addChild(BodyPart child) {
        _children.add(child);
        child.setParent(this);
    }

    public BodyPart getParent() {
        return _parent;
    }

    public void setParent(BodyPart parent) {
        _parent = parent;
    }

    public List<BodyPart> getChildren() {
        return _children;
    }

    public void calculateOrigins() {
        setPosition(BodyRules.getOrigin(this));
        if (_children != null && _children.size() > 0) {
            for (BodyPart part : _children) {
                part.calculateOrigins();
            }
        }
    }

    public int getHealth() {
        return _isAlive ? _health : 0;
    }

    public int getHealthMax() {
        return _healthMax;
    }
}