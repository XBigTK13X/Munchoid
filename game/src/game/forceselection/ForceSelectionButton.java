package game.forceselection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.forces.Force;
import game.ui.Meter;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.display.Screen;
import sps.entities.HitTest;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.ui.Buttons;
import sps.ui.ToolTip;
import sps.util.MathHelper;

public class ForceSelectionButton {
    private Force _force;
    private Meter _meter;
    private Creature _owner;
    private String _message;
    private Point2 _originalPosition;

    public ForceSelectionButton(Force force, Creature owner, int width, int height, Point2 origin, int row) {
        _owner = owner;
        _force = force;

        int stat = _owner.getStats().get(force);

        _originalPosition = new Point2(origin.X, (row * (int) (Screen.height(height) * 1.5)) + origin.Y);

        _meter = new Meter(width, height, force.Color, _originalPosition.add(0, 0), false);
        _meter.shade(Color.GRAY);
        _meter.scale(MathHelper.percent(stat / ((float) GameConfig.MaxStat)));

        _message = (stat > GameConfig.DisableStat && _owner.getStats().isEnabled(_force)) ? force.name() + ": " + strength() + Commands.get(force.Command) : "Disabled";
        ToolTip.get().add(new ToolTip.User() {
            @Override
            public boolean isActive() {
                return HitTest.inBox(Input.get().x(), Input.get().y(), _meter.getBounds());
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
        });

    }

    private void moveToRightSide() {
        _meter.setPosition(_originalPosition.X + Screen.width(50), _originalPosition.Y);
    }

    private void moveToLeftSide() {
        _meter.setPosition(_originalPosition.X, _originalPosition.Y);
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
