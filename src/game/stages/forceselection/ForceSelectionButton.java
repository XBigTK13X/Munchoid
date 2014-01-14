package game.stages.forceselection;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.config.GameConfig;
import game.stages.common.creatures.Creature;
import game.stages.common.forces.Force;
import sps.bridge.Commands;
import sps.color.Color;
import sps.core.Point2;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Buttons;
import sps.ui.Meter;
import sps.util.Maths;

public class ForceSelectionButton {
    private Force _force;
    private Meter _meter;
    private Text _display;
    private Creature _owner;
    private String _message;
    private Point2 _originalPosition;
    private int _messageOffsetX = 30;
    private int _messageOffsetY = 37;

    public ForceSelectionButton(Force force, Creature owner, int width, int height, Point2 origin, int row) {
        _owner = owner;
        _force = force;

        int stat = _owner.getStats().get(force);

        _originalPosition = new Point2(origin.X, (row * (int) (Screen.height(height) * 1.5)) + origin.Y);

        _meter = new Meter(width, height, force.Color, _originalPosition, false);
        _meter.shade(Color.GRAY);
        _meter.setPercent(Maths.percent(stat / ((float) GameConfig.MaxStat)));

        _message = force.name() + ": " + strength() + Commands.get(force.Command);
        _display = TextPool.get().write(_message, _originalPosition.add(_messageOffsetX, _messageOffsetY));
        _display.setFont("default", 30);

        if (!_owner.getStats().isEnabled(_force) || _owner.getStats().get(_force) <= GameConfig.DisableStat) {
            moveToRightSide();
        }

        Buttons.User user = new Buttons.User() {
            @Override
            public Sprite getSprite() {
                return _meter.getBackground();
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
        };
        user.setCommand(Commands.get(force.Command));
        user.setShouldDraw(false);
        Buttons.get().add(user);

    }

    private void moveToRightSide() {
        _meter.setPosition(_originalPosition.X + Screen.width(50), _originalPosition.Y);
        _display.setPosition((int) (_originalPosition.X + Screen.width(50) + _messageOffsetX), (int) _originalPosition.Y + _messageOffsetY);
    }

    private void moveToLeftSide() {
        _meter.setPosition(_originalPosition.X, _originalPosition.Y);
        _display.setPosition((int) (_originalPosition.X + _messageOffsetX), (int) _originalPosition.Y + _messageOffsetY);
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
        _meter.draw();
    }
}
