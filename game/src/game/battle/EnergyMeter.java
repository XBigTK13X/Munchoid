package game.battle;

import com.badlogic.gdx.graphics.Color;
import game.creatures.Creature;
import game.ui.Meter;
import sps.core.Point2;
import sps.display.Screen;

public class EnergyMeter {
    private static int __widthPercent = 5;

    private Creature _owner;
    private int _lastEnergy;
    private Meter _meter;

    public EnergyMeter(Creature owner) {
        _owner = owner;


        Point2 position;
        if (owner.getLocation().X < Screen.width(50)) {
            position = Screen.pos(5, 55);
        }
        else {
            position = Screen.pos(95 - __widthPercent, 55);
        }

        _meter = new Meter(__widthPercent, 40, Color.BLUE, position, true);
    }

    private void scaleHeight() {
        if (_lastEnergy == 0 || _lastEnergy != _owner.getEnergy()) {
            _meter.scale(_owner.getPercentEnergy());
            _lastEnergy = _owner.getEnergy();
        }
    }

    public void update() {
        scaleHeight();
    }

    public void draw() {
        _meter.draw();
    }
}
