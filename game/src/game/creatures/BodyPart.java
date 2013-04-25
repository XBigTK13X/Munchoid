package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.creatures.part.Common;
import game.creatures.part.Designs;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.Colors;

public class BodyPart {
    Atom[][] _atoms;
    int _width;
    int _height;
    float _percentRequiredToLive = .15f;
    boolean _isAlive = true;
    Color _color;
    float _scale;
    private PartFunction _function;

    private Point2 _position;

    public BodyPart(PartFunction function, int width, int height, Body owner) {
        _function = function;
        _atoms = new Atom[width][height];
        _width = width;
        _height = height;
        _scale = 1f;
        _color = owner.getColor();
        switch(RNG.next(0,3)){
            case 0:
                _color = _color;
                break;
            case 1:
                _color = Colors.darken(_color);
                break;
            case 2:
                _color = Colors.lighten(_color);
                break;
        }
        _position = new Point2(RNG.next(-width, width), RNG.next(-height, height));

        boolean[][] design = Designs.get(_function).create(_width, _height);
        design = Common.trim(design);
        _width = design.length;
        _height = design[0].length;


        //TODO Shading the outer edges w/ a 2px line
        //TODO Single color palette
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (design[ii][jj]) {
                    _atoms[ii][jj] = new Atom(ii, jj, _color, owner, this);
                }
            }
        }
        Outline.complimentary(_atoms);
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
    }

    public void draw() {
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (_atoms[ii][jj] != null && _atoms[ii][jj].isActive()) {
                    _atoms[ii][jj].draw();
                }
            }
        }
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
    }

    public void setPosition(Point2 position) {
        _position = position;
    }

    public PartFunction getFunction() {
        return _function;
    }
}
