package game.battle;

import game.creatures.Creature;
import game.forces.Force;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.Screen;

import java.util.ArrayList;
import java.util.List;

public class ForcesHUD {
    private static final int __widthPercent = 30;
    private static final int __heightPercent = 5;

    private Creature _owner;
    private List<ForceMeter> _meters;
    private Point2 _origin;

    public ForcesHUD(Creature owner) {
        _owner = owner;
        _origin = new Point2(owner.getLocation().X, Renderer.get().VirtualHeight - __heightPercent * Force.values().length - Screen.height(42));
        _meters = new ArrayList<ForceMeter>();
        int row = Force.values().length;
        for (Force force : Force.values()) {
            row--;
            _meters.add(new ForceMeter(force, _owner, __widthPercent, __heightPercent, _origin, row));
        }
    }

    public void draw() {
        for (ForceMeter meter : _meters) {
            meter.draw();
        }
    }

    public void update() {
        for (ForceMeter meter : _meters) {
            meter.update();
        }
    }
}
