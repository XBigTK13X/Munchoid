package game.creatures;

import com.badlogic.gdx.graphics.Color;
import sps.core.Point2;
import sps.core.RNG;
import sps.util.Colors;

public class BodyPart {
    Atom[][] _atoms;
    int _width;
    int _height;

    Color _color;

    private Point2 _position;

    public BodyPart(int width, int height, Body owner) {
        _atoms = new Atom[width][height];
        _width = width;
        _height = height;
        _color = Colors.random();
        _position = new Point2(RNG.next(-50, 50), RNG.next(-100, 100));
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                _atoms[ii][jj] = new Atom(ii, jj, _color, owner, this);
            }
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
}
