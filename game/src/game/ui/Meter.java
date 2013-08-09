package game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.SpriteMaker;

public class Meter {
    private Sprite _bg;
    private Sprite _sprite;
    private Point2 _position;

    private int _height;
    private int _width;

    private int _scaledWidth;


    public Meter(int screenWidthPercent, int screenHeightPercent, Color color) {
        _position = Screen.pos(5, 30);

        _width = (int) Screen.width(screenWidthPercent);
        _height = (int) Screen.height(screenHeightPercent);


        Color[][] bg = Colors.genArr(_width, _height, Color.LIGHT_GRAY);
        _bg = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(_height, _height, color);
        _sprite = SpriteMaker.get().fromColors(base);
        _bg.setSize(_width, _height);
        _bg.setPosition(_position.X, _position.Y);
        scaleWidth(0);
        _sprite.setPosition(_position.X, _position.Y);
    }

    public void scaleWidth(int percent) {
        _scaledWidth = (int) (_width * (percent / 100f));
        _sprite.setSize(_scaledWidth, _height);
    }

    public void draw() {
        Window.get().draw(_bg);
        Window.get().draw(_sprite);
    }
}
