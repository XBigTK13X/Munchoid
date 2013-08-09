package game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.SpriteMaker;
import sps.util.Bounds;

public class Meter {
    private Sprite _background;
    private Sprite _sprite;
    private Point2 _position;

    private int _height;
    private int _width;

    private int _scaledWidth;
    private int _scaledHeight;

    private Bounds _bounds;

    private boolean _isVertical;

    public Meter(int screenWidthPercent, int screenHeightPercent, Color color, Point2 position, boolean vertical) {
        _isVertical = vertical;
        _position = new Point2(0, 0);
        _width = (int) Screen.width(screenWidthPercent);
        _height = (int) Screen.height(screenHeightPercent);

        Color[][] bg = Colors.genArr(_width, _height, Color.LIGHT_GRAY);
        _background = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(_height, _height, color);
        _sprite = SpriteMaker.get().fromColors(base);
        _background.setSize(_width, _height);
        scale(0);
        setPosition(position.X, position.Y);
        //Outline.single(base, Color.WHITE, GameConfig.MeterOutlinePixelThickness);
    }

    private void scaleWidth(int percent) {
        _scaledWidth = (int) (_width * (percent / 100f));
        _sprite.setSize(_scaledWidth, _height);
    }

    private void scaleHeight(int percent) {
        _scaledHeight = (int) (_height * (percent / 100f));
        _sprite.setSize(_width, _scaledHeight);
    }

    public void scale(int percent) {
        if (_isVertical) {
            scaleHeight(100);
            scaleHeight(percent);
        }
        else {
            scaleWidth(100);
            scaleWidth(percent);
        }
    }

    public void draw() {
        Window.get().draw(_background);
        Window.get().draw(_sprite);
    }

    public void shade(Color color) {
        _sprite.setColor(color);
        _background.setColor(color);
    }

    public Sprite getBackground() {
        return _background;
    }

    public Bounds getBounds() {
        return _bounds;
    }

    public void setPosition(float x, float y) {
        _position.reset(x, y);
        _background.setPosition(_position.X, _position.Y);
        _sprite.setPosition(_position.X, _position.Y);
        _bounds = Bounds.fromDimensions(_position.X, _position.Y, _width, _height);
    }
}
