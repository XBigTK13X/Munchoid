package game.stages.forceselection;

import game.stages.common.creatures.Creature;
import game.stages.common.forces.Force;
import sps.core.Point2;
import sps.display.Screen;

import java.util.ArrayList;
import java.util.List;

public class ForcesSelectionUI {
    private static final int __widthPercent = 30;
    private static final int __heightPercent = 5;

    private Creature _owner;
    private List<ForceSelectionButton> _meters;
    private Point2 _origin;

    public ForcesSelectionUI(Creature owner) {
        _owner = owner;
        _origin = new Point2(Screen.width(10), Screen.get().VirtualHeight - __heightPercent * Force.values().length - Screen.height(48));
        _meters = new ArrayList<>();
        int row = Force.values().length;
        for (Force force : Force.values()) {
            row--;
            if (owner.getStats().canBeEnabled(force)) {
                _meters.add(new ForceSelectionButton(force, _owner, __widthPercent, __heightPercent, _origin, row));
            }
        }
    }

    public void draw() {
        for (ForceSelectionButton meter : _meters) {
            meter.draw();
        }
    }
}
