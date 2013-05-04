package game.battle;

import game.creatures.Creature;
import game.forces.Force;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.Screen;

import java.util.ArrayList;
import java.util.List;

public class ForcesHUD {
    private static final int __widthPercent = 15;
    private static final int __heightPercent = 5;

    private Creature _owner;
    private List<ForceMeter> _backgrounds;
    private Point2 _origin;

    public ForcesHUD(Creature owner) {
        _owner = owner;
        _origin = new Point2(owner.getLocation().X, Renderer.get().VirtualHeight - __heightPercent * Force.values().length - Screen.height(40));
        _backgrounds = new ArrayList<ForceMeter>();
        for (Force force : Force.values()) {
            _backgrounds.add(new ForceMeter(force, _owner, __widthPercent, __heightPercent));
        }
    }

    public void draw() {
        int row = Force.values().length;
        for (ForceMeter meter : _backgrounds) {
            row--;
            meter.draw(_origin, row);
        }
    }
}
