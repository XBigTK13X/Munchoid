package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.Creature;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.graphics.Window;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class EnergyMeter {
    private static int __widthPercent = 5;
    private static int __heightPercent = 40;

    private Creature _owner;
    private Sprite _bg;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private Point2 _position;
    private int _scaledHeight;

    private int _lastEnergy;

    public EnergyMeter(Creature owner) {
        _owner = owner;
        _width = (int) Screen.width(__widthPercent);
        _height = (int) Screen.height(__heightPercent);
        if (owner.getLocation().X < Screen.width(50)) {
            _position = Screen.pos(5, 55);
        }
        else {
            _position = Screen.pos(95 - __widthPercent, 55);
        }

        Color[][] bg = Colors.genArr(__widthPercent, __heightPercent, Color.LIGHT_GRAY);
        _bg = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(__widthPercent, __heightPercent, Color.BLUE);
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);
        _bg.setSize(_width, _height);
        _bg.setPosition(_position.X, _position.Y);
        scaleHeight();
        _sprite.setPosition(_position.X, _position.Y);
    }

    private void scaleHeight() {
        if (_lastEnergy == 0 || _lastEnergy != _owner.getEnergy()) {
            _scaledHeight = (int) (_height * _owner.getPercentEnergy());
            _sprite.setSize(_width, _scaledHeight);
            _lastEnergy = _owner.getEnergy();
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
