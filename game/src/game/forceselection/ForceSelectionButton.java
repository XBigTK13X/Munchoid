package game.forceselection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import game.states.ForceSelection;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.entities.HitTest;
import sps.graphics.Window;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.ui.Buttons;
import sps.ui.ToolTip;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class ForceSelectionButton {
    private Sprite _bg;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private int _scaledWidth;
    private Point2 _position;
    private Force _force;

    private Creature _owner;

    private String _message;

    public ForceSelectionButton(Force force, Creature owner, int width, int height, Point2 origin, int row) {
        _owner = owner;
        _force = force;
        _width = (int) Screen.width(width);
        _height = (int) Screen.height(height);
        _position = new Point2(origin.X, (row * (int) (_height * 1.5)) + origin.Y);
        int stat = _owner.getStats().get(force);
        float statPercent = stat / ((float) GameConfig.MaxStat);
        _scaledWidth = (int) (statPercent * _width);

        Color[][] bg = Colors.genArr(width, height, Color.LIGHT_GRAY);
        _bg = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(width, height, force.Color);
        Outline.single(base, Color.WHITE);
        _sprite = SpriteMaker.get().fromColors(base);

        _bg.setSize(_width, _height);
        _bg.setPosition(_position.X, _position.Y);
        _sprite.setSize(_scaledWidth, _height);
        _sprite.setPosition(_position.X, _position.Y);

        _bg.setColor(Color.GRAY);
        _sprite.setColor(Color.GRAY);
        String input = "[" + Commands.get(force.Command).key().name() + "]";

        _message = (stat > GameConfig.DisableStat && _owner.getStats().isEnabled(_force)) ? force.name() + ": " + strength() + input : "Disabled";
        ToolTip.get().add(new ToolTip.User() {
            @Override
            public boolean isActive() {
                return HitTest.inBox(Input.get().x(), Input.get().y(), (int) _sprite.getX(), (int) _sprite.getY(), _width, _height);
            }

            @Override
            public String message() {
                setTooltip();
                return _message;
            }
        });


        if (!_owner.getStats().isEnabled(_force) || _owner.getStats().get(_force) <= GameConfig.DisableStat) {
            moveToRightSide();
        }
        Buttons.get().add(new Buttons.User() {
            @Override
            public Sprite getSprite() {
                return _bg;
            }

            @Override
            public void onClick() {
                _owner.getStats().toggleEnabled(_force);
                if (!_owner.getStats().isEnabled(_force)) {
                    moveToRightSide();
                }
                else {
                    moveToLeftSide();
                }
            }
        });

    }

    private void moveToRightSide() {
        _bg.setPosition(_position.X + Screen.width(50), _position.Y);
        _sprite.setPosition(_position.X + Screen.width(50), _position.Y);
    }

    private void moveToLeftSide() {
        _bg.setPosition(_position.X, _position.Y);
        _sprite.setPosition(_position.X, _position.Y);
    }

    private String strength() {
        return "(" + _owner.getStats().get(_force) + "/" + GameConfig.MaxStat + ")";
    }

    private void setTooltip() {
        State state = StateManager.get().current();
        if (state instanceof ForceSelection) {
            _message = _force.name() + ": " + ((_owner.getStats().isEnabled(_force)) ? "ENABLED: " + strength() : "DISABLED" + strength());
        }
    }

    public void draw() {
        Window.get().draw(_bg);
        Window.get().draw(_sprite);
    }
}
