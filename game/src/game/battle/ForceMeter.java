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
import sps.ui.ToolTip;
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

    private String _message;

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
        }

        Color[][] base = Colors.genArr(width, height, force.Color);
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);

        String input = "[" + Commands.get(force.Command).key().name() + "]";
        String strength = "(" + stat + "/" + GameConfig.MaxStat + ")";

        _message = force.name() + ": " + strength + input;

        ToolTip.get().add(new ToolTip.User() {
            @Override
            public boolean isActive() {
                return _position.X < Screen.width(50) && HitTest.inBox(Input.get().x(), Input.get().y(), (int) _position.X, (int) _position.Y, _width, _height);
            }

            @Override
            public String message() {
                return _message;
            }
        });

    }

    public void draw() {
        Renderer.get().draw(__bg, _position, DrawDepths.get("ForceMeter"), Color.WHITE, _width, _height);
        Renderer.get().draw(_sprite, _position, DrawDepths.get("ForceMeter"), Color.WHITE, _scaledWidth, _height);
    }
}
