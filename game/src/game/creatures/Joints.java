package game.creatures;

import com.badlogic.gdx.graphics.Color;
import sps.core.Point2;
import sps.display.DrawAPICall;
import sps.display.Window;

import java.util.ArrayList;
import java.util.List;

public class Joints {
    private BodyPart _owner;
    private List<Joint> _joints;

    public Joints(BodyPart owner) {
        _owner = owner;
        _joints = new ArrayList<Joint>();
    }

    public void add(Joint joint) {
        _joints.add(joint);
    }

    public List<Joint> getAll() {
        return _joints;
    }

    public void draw() {
        for (Joint j : _joints) {
            Window.get().schedule(new DrawAPICall(Color.GREEN, j.getGlobalCenter().X, j.getGlobalCenter().Y, 10));
            if (j.getChild() != null) {
                //TODO Figure out which joint we are connected to, and draw a line to that.
                //Window.get().schedule(new DrawAPICall(Color.YELLOW, jgetGlobalCenter().X, j.getGlobalCenter().Y, j.getGlobalCenter().X, j.getGlobalCenter().Y));
                j.getChild().getJoints().draw();
            }
        }
    }

    public Point2 getGridConnectionTo(BodyPart part) {
        for (Joint j : _joints) {
            if (j.getChild() == part) {
                return new Point2(j.GridLoc, j.getChild().getFunction().LocAttach);
            }
        }
        return null;
    }
}
