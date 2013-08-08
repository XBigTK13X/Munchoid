package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import game.states.Battle;
import game.states.ForceSelection;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.SpriteMaker;
import sps.entities.HitTest;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.ui.Buttons;
import sps.ui.ToolTip;

public class ForceMeter {
    private Sprite _bg;
    private Sprite _sprite;
    private int _height;
    private int _width;
    private int _scaledWidth;
    private Point2 _position;
    private Force _force;

    private Creature _owner;

    private String _message;

    public ForceMeter(Force force, Creature owner, int width, int height, Point2 origin) {
        _owner = owner;
        _force = force;
        _width = (int) Screen.width(width);
        _height = (int) Screen.height(height);
        _position = origin;
        int stat = _owner.getStats().get(force);
        float statPercent = stat / ((float) GameConfig.MaxStat);
        _scaledWidth = (int) (statPercent * _width);

        Color[][] bg = Colors.genArr(width, height, Color.LIGHT_GRAY);
        _bg = SpriteMaker.get().fromColors(bg);

        Color[][] base = Colors.genArr(width, height, force.Color);
        Outline.single(base, Color.WHITE, GameConfig.MeterOutlinePixelThickness);
        _sprite = SpriteMaker.get().fromColors(base);

        _bg.setSize(_width, _height);
        _bg.setPosition(_position.X, _position.Y);
        _sprite.setSize(_scaledWidth, _height);
        _sprite.setPosition(_position.X, _position.Y);


        Color core = new Color(Color.GRAY);
        if (!owner.getStats().isEnabled(force)) {
            core.a = .33f;
        }
        _bg.setColor(core);
        _sprite.setColor(core);

        boolean isPlayer = _position.X < Screen.width(50);
        if (isPlayer) {
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

            if (stat > GameConfig.DisableStat && _owner.getStats().isEnabled(_force)) {
                Buttons.get().add(new Buttons.User() {
                    @Override
                    public Sprite getSprite() {
                        return _bg;
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
        Window.get().draw(_bg);
        Window.get().draw(_sprite);
    }
}
