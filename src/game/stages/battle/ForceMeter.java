package game.stages.battle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.config.GameConfig;
import game.app.dev.DevConfig;
import game.stages.common.creatures.Creature;
import game.stages.common.forces.Force;
import game.stages.forceselection.ForceSelection;
import sps.bridge.Commands;
import sps.color.Color;
import sps.core.Point2;
import sps.display.Screen;
import sps.entities.HitTest;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.ui.Buttons;
import sps.ui.Meter;
import sps.ui.Tooltips;
import sps.util.Maths;

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

        Color core = Color.GRAY;
        if (!owner.getStats().isEnabled(force)) {
            core = core.newAlpha(.33f);
        }

        _meter = new Meter(width, height, force.Color, _position, false);
        _meter.shade(core);

        _meter.setPercent(Maths.percent(stat / ((float) GameConfig.MaxStat)));

        boolean isPlayer = _position.X < Screen.width(50);
        if (isPlayer) {
            _message = (stat > GameConfig.DisableStat && _owner.getStats().isEnabled(_force)) ? force.name() + ": " + strength() + Commands.get(force.Command) : "Disabled";
            Tooltips.get().add(new Tooltips.User() {
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
                Buttons.User user = new Buttons.User() {
                    @Override
                    public Sprite getSprite() {
                        return _meter.getBackground();
                    }

                    @Override
                    public void onClick() {
                        State state = StateManager.get().current();
                        if (DevConfig.MeterTest == false) {
                            Battle battle = (Battle) state;
                            battle.playerActivate(_force);
                        }
                    }

                    @Override
                    public void over() {
                        super.over();
                        State state = StateManager.get().current();
                        if (DevConfig.MeterTest == false) {
                            Battle battle = (Battle) state;
                            battle.playerShowCost(_force);
                        }
                    }
                };
                user.setCommand(Commands.get(force.Command));
                user.setShouldDraw(false);
                Buttons.get().add(user);
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
