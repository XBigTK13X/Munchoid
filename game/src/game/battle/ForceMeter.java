package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import sps.bridge.Commands;
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
    private static Sprite __bg;

    private Sprite _sprite;
    private int _height;
    private int _width;
    private int _scaledWidth;
    private Point2 _position;

    private static final Point2 __tooltipOffset = Screen.pos(1, 1);
    private static Sprite __tooltipBg;
    private String _meterMessage;
    private Text _messageDisplay;
    private boolean tooltipActive;
    private Point2 _tooltipPosition = new Point2(0, 0);
    private int _messageWidth;
    private static final int __fontWidth = 24;


    public ForceMeter(Force force, Creature owner, int width, int height, Point2 origin, int row) {

        _width = (int) Screen.width(width);
        _height = (int) Screen.height(height);
        _position = new Point2(origin.X, (row * (int) (_height * 1.5)) + origin.Y);
        int stat = owner.getStats().get(force);
        float statPercent = stat / ((float) GameConfig.MaxStat);
        _scaledWidth = (int) (statPercent * _width);

        if (__bg == null) {
            Color[][] bg = Colors.genArr(width, height, Color.LIGHT_GRAY);
            __bg = SpriteMaker.get().fromColors(bg);

            Color[][] tbg = Colors.genArr(width, _height, new Color(.1f, .1f, .1f, .7f));
            __tooltipBg = SpriteMaker.get().fromColors(tbg);
        }

        Color[][] base = Colors.genArr(width, height, force.Color);
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);

        String input = "[" + Commands.get(force.Command).key().name() + "]";
        String strength = "(" + stat + "/" + GameConfig.MaxStat + ")";

        _meterMessage = force.name() + ": " + strength + input;
        _messageWidth = (int) (_meterMessage.length() * __fontWidth);
        _messageDisplay = TextPool.get().write(_meterMessage, _position.add(_width, (int) (_height * .7f)));
        _messageDisplay.hide();
    }

    public void draw() {
        Renderer.get().draw(__bg, _position, DrawDepths.get("ForceMeter"), Color.WHITE, _width, _height);
        Renderer.get().draw(_sprite, _position, DrawDepths.get("ForceMeter"), Color.WHITE, _scaledWidth, _height);
        if (tooltipActive) {
            Renderer.get().draw(__tooltipBg, _tooltipPosition.add(-__fontWidth / 2, -_height + _height / 4), DrawDepths.get("TooltipBackground"), Color.WHITE, _messageWidth, __tooltipBg.getHeight());
        }
    }

    public void update() {
        if (_position.X < Screen.width(50)) {
            tooltipActive = HitTest.inBox(Input.get().x(), Input.get().y(), (int) _position.X, (int) _position.Y, _width, _height);
            if (tooltipActive) {
                _tooltipPosition.reset(Input.get().x() + (int) __tooltipOffset.X, Input.get().y() + (int) __tooltipOffset.Y);
                _messageDisplay.setPosition((int) _tooltipPosition.X, (int) _tooltipPosition.Y);
            }
            _messageDisplay.setVisible(tooltipActive);
        }
    }
}
