package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.entities.HitTest;
import sps.graphics.Renderer;
import sps.io.Input;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class ForceMeter {
    private static Sprite _background;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private int _scaledWidth;
    private Point2 _position;
    private String _meterMessage;
    private Text _messageDisplay;

    public ForceMeter(Force force, Creature owner, int width, int height, Point2 origin, int row) {

        _width = (int) Screen.width(width);
        _height = (int) Screen.height(height);
        _position = new Point2(origin.X, row * _height - _height + origin.Y);
        _scaledWidth = (int) ((owner.getStats().get(force) / (float) GameConfig.MaxStat) * _width);

        if (_background == null) {
            Color[][] bg = Colors.genArr(width, height, Color.LIGHT_GRAY);
            _background = SpriteMaker.get().fromColors(bg);
        }

        Color[][] base = Colors.genArr(width, height, force.Color);
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);

        _meterMessage = force.name();
        _messageDisplay = TextPool.get().write(_meterMessage, _position.add(_width, _height));
        _messageDisplay.hide();
    }

    public void draw() {
        Renderer.get().draw(_background, _position, DrawDepths.get("ForceMeter"), Color.WHITE, _width, _height);
        Renderer.get().draw(_sprite, _position, DrawDepths.get("ForceMeter"), Color.WHITE, _scaledWidth, _height);
    }

    public void update() {
        _messageDisplay.setVisible(HitTest.inBox(Input.get().mouseX(), Input.get().mouseY(), (int) _position.X, (int) _position.Y, _width, _height));
    }
}
