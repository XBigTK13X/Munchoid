package game.battle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.util.Maths;

public class TimerGraphic {

    private static final Sprite[] _frames = new Sprite[101];

    private int _percent;
    private boolean _fillUp;
    private Point2 _position;
    private boolean _finished;
    private boolean _visible;
    private Color _color;


    public TimerGraphic(boolean fillUp, Point2 position, Color color) {
        if (_frames[0] == null) {
            int radiusPixels = (int) Screen.width(5);
            Color[][] base;
            for (int ii = 0; ii <= 100; ii++) {
                float rotationMax = Maths.percentToValue(0, 360, ii);
                base = ProcTextures.centeredCircleSegment(0, radiusPixels, 0, (int) rotationMax, Color.WHITE);
                Outline.single(base, Color.BLACK, 3);
                _frames[ii] = SpriteMaker.fromColors(base);
                _frames[ii].setRotation(-90);
            }
        }
        _color = color;
        _fillUp = fillUp;
        _visible = true;
        reset();
        _position = position;
    }

    public void setPercent(int percent) {
        _percent = _fillUp ? percent : 100 - percent;
        _percent = Maths.clamp(_percent, 0, 100);
        _finished = (_fillUp ? (_percent == 100) : (_percent == 0));
    }

    public int getPercent() {
        return _fillUp ? _percent : 100 - _percent;
    }

    public void reset() {
        _percent = _fillUp ? 0 : 100;
        _finished = false;
    }

    public boolean isFinished() {
        return _finished;
    }

    public void setVisible(boolean visible) {
        _visible = visible;
    }


    public void draw() {
        if (_visible) {
            _frames[_percent].setColor(_color.getGdxColor());
            _frames[_percent].setPosition(_position.X, _position.Y);
            Window.get().schedule(_frames[_percent], DrawDepths.get("BattleCoolDown"));
        }
    }
}
