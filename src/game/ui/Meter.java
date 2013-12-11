package game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.style.Outline;
import sps.bridge.DrawDepths;
import sps.color.Colors;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.util.BoundingBox;

public class Meter {
    private Sprite _background;
    private Sprite _sprite;
    private Sprite _frame;

    private Point2 _position;
    private int _height;
    private int _width;
    private int _scaledWidth;
    private int _scaledHeight;
    private BoundingBox _boundingBox = BoundingBox.empty();
    private boolean _isVertical;

    public Meter(int screenWidthPercent, int screenHeightPercent, Color color, Point2 position, boolean vertical) {
        _isVertical = vertical;
        _position = new Point2(0, 0);
        _width = (int) Screen.width(screenWidthPercent);
        _height = (int) Screen.height(screenHeightPercent);

        Color[][] bg = ProcTextures.gradient(_width, _height, Color.LIGHT_GRAY, Colors.darken(Color.LIGHT_GRAY), !vertical);
        _background = SpriteMaker.get().fromColors(bg);
        _background.flip(true, true);

        Color[][] base = ProcTextures.gradient(_width, _height, Colors.lighten(color), Colors.darken(color), !vertical);
        _sprite = SpriteMaker.get().fromColors(base);

        Color[][] frame = ProcTextures.monotone(_width, _height, Color.BLACK);
        Outline.single(frame, Color.WHITE, GameConfig.MeterOutlinePixelThickness);
        ProcTextures.remove(frame, Color.BLACK);
        _frame = SpriteMaker.get().fromColors(frame);

        scale(0);
        setPosition(position.X, position.Y);

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
        Window.get().schedule(_background, DrawDepths.get("MeterBackground"));
        Window.get().schedule(_sprite, DrawDepths.get("Meter"));
        Window.get().schedule(_frame, DrawDepths.get("MeterFrame"));
    }

    public void shade(Color color) {
        _sprite.setColor(color);
        _background.setColor(color);
        _frame.setColor(color);
    }

    public Sprite getBackground() {
        return _background;
    }

    public BoundingBox getBounds() {
        return _boundingBox;
    }

    public void setPosition(float x, float y) {
        _position.reset(x, y);
        _background.setPosition(_position.X, _position.Y);
        _sprite.setPosition(_position.X, _position.Y);
        _frame.setPosition(_position.X, _position.Y);
        BoundingBox.fromDimensions(_boundingBox, _position.X, _position.Y, _width, _height);
    }
}
