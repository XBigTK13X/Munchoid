package game.battle;

import game.creatures.Creature;
import game.ui.Meter;
import sps.color.Color;
import sps.core.Point2;
import sps.display.Screen;
import sps.util.CoolDown;

public class CostMeter {
    private static int __widthPercent = 5;

    private int _lastCost;
    private Meter _meter;
    private CoolDown _flash = new CoolDown(.5f);

    public CostMeter(Creature owner) {
        Point2 position;
        if (owner.getLocation().X < Screen.width(50)) {
            position = Screen.pos(5, 55);
        }
        else {
            position = Screen.pos(95 - __widthPercent, 55);
        }

        _meter = new Meter(__widthPercent, 40, Color.YELLOW, position, true);
    }

    public void scaleHeight(int costPercent) {
        if (_lastCost == 0 || _lastCost != costPercent) {
            _meter.scale(costPercent);
            _lastCost = costPercent;
        }
    }

    public void draw() {
        _flash.update();
        if (!_flash.isCooled()) {
            _meter.draw();
        }
    }

    public void show() {
        _flash.reset();
    }
}
