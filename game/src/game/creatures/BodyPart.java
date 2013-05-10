package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.part.Common;
import game.creatures.part.Designs;
import game.creatures.style.BodyRules;
import game.creatures.style.Outline;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.Colors;
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

    public BodyPart(PartFunction function, int width, int height, Body owner) {
        _parent = null;
        _children = new ArrayList<BodyPart>();
        _function = function;
        _owner = owner;
        _scale = 1f;
        _color = owner.getColor();
        _position = new Point2(0, 0);

        boolean[][] design = Designs.get(_function).create(width, height);
        design = Common.trim(design);
        _width = design.length;
        _height = design[0].length;
        //TODO Remove Logger.info("PART: " +function.name() + ", "+_width +" . " +_height);

        _atoms = new Atom[_width][_height];
        //TODO Shading the outer edges w/ a 2px line
        //TODO Single color palette
        Color[][] textureBase = getTextureBase();
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (design[ii][jj]) {
                    _atoms[ii][jj] = new Atom(ii, jj, textureBase[ii][jj]);
                }
            }
        }
        applyStyle();
    }

    private Color[][] getTextureBase() {
        return Colors.getPerlinGrid(_width, _height, Colors.darken(_color), Colors.lighten(_color));
    }

    public BodyPart(BodyPart source, Body owner, Color color) {
        _function = source.getFunction();
        _owner = owner;
        _scale = 1f;
        _color = color;
        _atoms = AtomHelper.copy(source.getAtoms());
        _position = source.getPosition();
        _width = source.getWidth();
        _height = source.getHeight();
        Color[][] textureBase = getTextureBase();
        for (int ii = 0; ii < _atoms.length; ii++) {
            for (int jj = 0; jj < _atoms[0].length; jj++) {
                _atoms[ii][jj].setColor(textureBase[ii][jj]);
            }
        }
        applyStyle();
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
        if (currentActive / (float) maxActive <= _percentRequiredToLive) {
            _isAlive = false;
        }
        if (_scale <= GameConfig.MinScaleDeath || _scale >= GameConfig.MaxScaleDeath) {
            _isAlive = false;
        }
        createSprite();
    }

    public void draw() {
        Point2 scaledLoc = new Point2((getPosition().X + (_parent != null ? _parent.getPosition().X : 0)) * _scale, (getPosition().Y + (_parent != null ? _parent.getPosition().Y : 0)) * _scale);
        scaledLoc = scaledLoc.addRaw(_owner.getOwner().getLocation());

        Renderer.get().draw(_sprite, scaledLoc, DrawDepths.get("Atom"), Color.WHITE, _width * _scale, _height * _scale);
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

    public void calculateOrigins() {
        setPosition(BodyRules.getOrigin(this));
        if (_children != null && _children.size() > 0) {
            for (BodyPart part : _children) {
                part.calculateOrigins();
            }
        }
    }
}