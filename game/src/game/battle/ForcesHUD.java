package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.Creature;
import game.creatures.style.Outline;
import game.forces.Force;
import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.graphics.Renderer;
import sps.util.Screen;
import sps.util.SpriteMaker;

import java.util.ArrayList;
import java.util.List;

public class ForcesHUD {
    private Creature _owner;
    private List<Sprite> _backgrounds;
    private static final int __widthPercent = 15;
    private static final int __heightPercent = 3;
    private Point2 _origin;

    public ForcesHUD(Creature owner) {
        _owner = owner;
        _origin = new Point2(owner.getLocation().X, Renderer.get().VirtualHeight - __heightPercent * Force.values().length - Screen.height(40));
        buildMeters();
    }

    private Sprite buildMeter(Force force) {

        Color[][] base = new Color[__widthPercent][__heightPercent];
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                base[ii][jj] = force.Color;
            }
        }
        Outline.complimentary(base);
        return SpriteMaker.get().fromColors(base);
    }

    private void buildMeters() {
        _backgrounds = new ArrayList<Sprite>();
        for (Force force : Force.values()) {
            _backgrounds.add(buildMeter(force));
        }
    }

    public void draw() {
        int ii = 0;
        Point2 dims = Screen.pos(__widthPercent, __heightPercent);
        for (Sprite bg : _backgrounds) {
            ii++;
            Renderer.get().draw(bg, new Point2(_origin.X, ii * dims.Y - dims.Y + _origin.Y), DrawDepths.get("ForceMeter"), Color.WHITE, dims.X, dims.Y);
        }
    }
}
