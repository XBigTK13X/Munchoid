package game.creatures;

import com.badlogic.gdx.graphics.Color;
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

    private Point2 _position;

    public BodyPart(int width, int height, Body owner) {
        _atoms = new Atom[width][height];
        _width = width;
        _height = height;
        _scale = 1f;
        _color = Colors.random();
        _position = new Point2(RNG.next(-width, width), RNG.next(-height, height));
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                _atoms[ii][jj] = new Atom(ii, jj, _color, owner, this);
            }
        }

    }

    public Atom[][] getAtoms() {
        return _atoms;
    }

    public void setAtoms(Atom[][] atoms) {
        _atoms = atoms;
        int maxActive = _width * _height;
        int currentActive = 0;
        for (int ii = 0; ii < _width; ii++) {
            for (int jj = 0; jj < _height; jj++) {
                if (atoms[ii][jj] != null) {
                    currentActive++;
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
                if (_atoms[ii][jj] != null) {
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
}
