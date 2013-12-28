package game.battle;

import game.creatures.Creature;
import sps.color.Color;
import sps.core.Point2;
import sps.display.Screen;
import sps.ui.Meter;

public class HealthMeter {
    private static int __widthPercent = 5;

    Meter _meter;
    private Creature _owner;
    private int _lastHealth;

    public HealthMeter(Creature owner) {
        _owner = owner;
        Point2 position;
        if (owner.getLocation().X < Screen.width(50)) {
            position = Screen.pos(5, 5);
        }
        else {
            position = Screen.pos(95 - __widthPercent, 5);
        }

        _meter = new Meter(__widthPercent, 40, Color.GREEN, position, true);
    }

    private void scaleHeight() {
        if (_lastHealth == 0 || _lastHealth != _owner.getBody().getHealth()) {
            _meter.scale(_owner.getBody().getPercentHealth());
            _lastHealth = _owner.getBody().getHealth();
        }
    }

    public void update() {
        scaleHeight();
    }

    public void draw() {
        _meter.draw();
    }
}
