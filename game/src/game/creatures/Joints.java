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
        Window.get().schedule(new DrawAPICall(Color.GREEN, _owner.getGlobalPosition().X, _owner.getGlobalPosition().Y, 10));
        for (Joint j : _joints) {
            if (j.getChild() != null) {
                Window.get().schedule(new DrawAPICall(Color.YELLOW, _owner.getGlobalPosition().X, _owner.getGlobalPosition().Y, j.getChild().getGlobalPosition().X, j.getChild().getGlobalPosition().Y));
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
