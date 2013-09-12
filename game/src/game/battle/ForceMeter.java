package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.forces.Force;
import game.forceselection.ForceSelection;
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

public class ForceMeter {
    private Force _force;
    private Creature _owner;
    private String _message;
    private Meter _meter;
    private Point2 _position;

    public ForceMeter(Force force, Creature owner, int width, int height, Point2 origin) {
        _owner = owner;
        _force = force;
        _position = origin;
        int stat = _owner.getStats().get(force);

        Color core = new Color(Color.GRAY);
        if (!owner.getStats().isEnabled(force)) {
            core.a = .33f;
        }

        _meter = new Meter(width, height, force.Color, _position, false);
        _meter.shade(core);

        _meter.scale(MathHelper.percent(stat / ((float) GameConfig.MaxStat)));

        boolean isPlayer = _position.X < Screen.width(50);
        if (isPlayer) {
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

            if (stat > GameConfig.DisableStat && _owner.getStats().isEnabled(_force)) {
                Buttons.get().add(new Buttons.User() {
                    @Override
                    public Sprite getSprite() {
                        return _meter.getBackground();
                    }

                    @Override
                    public void onClick() {
                        State state = StateManager.get().current();

                        Battle battle = (Battle) state;
                        battle.playerActivate(_force);
                    }
                });
            }
        }
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
