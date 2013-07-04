package game.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.graphics.Window;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class LoadingMeter {
    private static int __widthPercent = 90;
    private static int __heightPercent = 5;

    private Sprite _bg;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private Point2 _position;
    private int _scaledWidth;


    public LoadingMeter() {
        _width = (int) Screen.width(__widthPercent);
        _height = (int) Screen.height(__heightPercent);

        _position = Screen.pos(5, 30);

        Color[][] bg = Colors.genArr(__widthPercent, __heightPercent, Color.LIGHT_GRAY);
        _bg = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(__widthPercent, __heightPercent, Color.BLUE);
        Outline.single(base, Color.WHITE);
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
