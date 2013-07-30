package game.battle;

import game.creatures.Creature;
import game.forces.Force;
import sps.core.Point2;
import sps.display.Screen;

import java.util.ArrayList;
import java.util.List;

public class ForcesHUD {
    private static final int __widthPercent = 10;
    private static final int __heightPercent = 7;
    private static final int __paddingPercent = 2;

    private Creature _owner;
    private List<ForceMeter> _meters;
    private Point2 _origin;

    private static final Point2[] __order = {new Point2(0, 0), new Point2(0, 1), new Point2(1, 0), new Point2(1, 1), new Point2(2, 0), new Point2(2, 1)};

    public ForcesHUD(Creature owner) {
        _owner = owner;
        if (_owner.getLocation().X < Screen.width(50)) {
            _origin = Screen.pos(15, 90);
        }
        else {
            _origin = Screen.pos(55, 90);
        }

        _meters = new ArrayList<ForceMeter>();
        int forceOrder = 0;
        for (Force force : Force.values()) {
            Point2 origin = _origin.add(__order[forceOrder].X * Screen.width(__widthPercent) + __order[forceOrder].X * Screen.width(__paddingPercent), -__order[forceOrder].Y * Screen.height(__heightPercent) - __order[forceOrder].Y * Screen.height(__paddingPercent));
            _meters.add(new ForceMeter(force, _owner, __widthPercent, __heightPercent, origin));
            forceOrder++;
        }
    }

    public void draw() {
        for (ForceMeter meter : _meters) {
            meter.draw();
        }
    }
}
