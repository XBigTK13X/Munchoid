package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.SpriteMaker;

public class HealthMeter {
    private static int __widthPercent = 5;
    private static int __heightPercent = 40;

    private Creature _owner;
    private Sprite _bg;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private Point2 _position;
    private int _scaledHeight;

    private int _lastHealth;

    public HealthMeter(Creature owner) {
        _owner = owner;
        _width = (int) Screen.width(__widthPercent);
        _height = (int) Screen.height(__heightPercent);
        if (owner.getLocation().X < Screen.width(50)) {
            _position = Screen.pos(5, 5);
        }
        else {
            _position = Screen.pos(95 - __widthPercent, 5);
        }

        Color[][] bg = Colors.genArr(__widthPercent, __heightPercent, Color.LIGHT_GRAY);
        _bg = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(__widthPercent, __heightPercent, Color.GREEN);
        Outline.single(base, Color.WHITE, GameConfig.MeterOutlinePixelThickness);
        _sprite = SpriteMaker.get().fromColors(base);
        _bg.setSize(_width, _height);
        _bg.setPosition(_position.X, _position.Y);
        scaleHeight();
        _sprite.setPosition(_position.X, _position.Y);
    }

    private void scaleHeight() {
        if (_lastHealth == 0 || _lastHealth != _owner.getBody().getHealth()) {
            _scaledHeight = (int) (_height * _owner.getBody().getPercentHealth());
            _sprite.setSize(_width, _scaledHeight);
            _lastHealth = _owner.getBody().getHealth();
        }
    }

    public void update() {
        scaleHeight();
    }

    public void draw() {
        Window.get().draw(_bg);
        Window.get().draw(_sprite);
    }
}
