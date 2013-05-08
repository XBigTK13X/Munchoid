package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.part.Common;
import game.creatures.part.Designs;
import game.creatures.style.Outline;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.core.RNG;
import sps.graphics.Renderer;
import sps.util.Colors;
import sps.util.SpriteMaker;

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

    public BodyPart(PartFunction function, int width, int height, Body owner) {
        _function = function;
        _owner = owner;
        _scale = 1f;
        chooseColor(owner.getColor());
        _position = new Point2(0, 0);

        boolean[][] design = Designs.get(_function).create(width, height);
        design = Common.trim(design);
        _width = design.length;
        _height = design[0].length;
        //TODO Remove Logger.info("PART: " +function.name() + ", "+_width +" . " +_height);


        _atoms = new Atom[_width][_height];
        //TODO Shading the outer edges w/ a 2px line
        //TODO Single color palette
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (design[ii][jj]) {
                    _atoms[ii][jj] = new Atom(ii, jj, _color);
                }
            }
        }
        applyStyle();
    }

    public BodyPart(BodyPart source, Body owner, Color color) {
        _function = source.getFunction();
        _owner = owner;
        _scale = 1f;
        chooseColor(color);
        _atoms = AtomHelper.copy(source.getAtoms());
        AtomHelper.setColor(_atoms, _color);
        applyStyle();
    }

    private void chooseColor(Color base) {
        _color = base;
        switch (RNG.next(0, 3)) {
            case 0:
                break;
            case 1:
                _color = Colors.darken(_color);
                break;
            case 2:
                _color = Colors.lighten(_color);
                break;
        }
    }

    private void applyStyle() {
        Color[][] atomColors = AtomHelper.getColors(_atoms);
        Outline.complimentary(atomColors);
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
        Point2 scaledLoc = new Point2(getPosition().X * _scale, getPosition().Y * _scale);
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
}
