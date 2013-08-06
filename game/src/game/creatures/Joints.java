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
            if (j.getChild() != null) {
                int radBG = 7;
                int radFG = radBG - 2;

                Color bg = Color.BLACK;
                Color fg = Color.WHITE;
                Color lbg = Color.WHITE;
                Color lfg = Color.BLACK;
                int lO = 2;

                Window.get().schedule(new DrawAPICall(bg, j.getGlobalCenter().X, j.getGlobalCenter().Y, radBG));
                Window.get().schedule(new DrawAPICall(fg, j.getGlobalCenter().X, j.getGlobalCenter().Y, radFG));

                Window.get().schedule(new DrawAPICall(lbg, j.getGlobalCenter().X - lO, j.getGlobalCenter().Y - lO, j.getChildGlobalCenter().X - lO, j.getChildGlobalCenter().Y - lO));
                Window.get().schedule(new DrawAPICall(lfg, j.getGlobalCenter().X, j.getGlobalCenter().Y, j.getChildGlobalCenter().X, j.getChildGlobalCenter().Y));
                Window.get().schedule(new DrawAPICall(lbg, j.getGlobalCenter().X + lO, j.getGlobalCenter().Y + lO, j.getChildGlobalCenter().X + lO, j.getChildGlobalCenter().Y + lO));

                Window.get().schedule(new DrawAPICall(bg, j.getChildGlobalCenter().X, j.getChildGlobalCenter().Y, radBG));
                Window.get().schedule(new DrawAPICall(fg, j.getChildGlobalCenter().X, j.getChildGlobalCenter().Y, radFG));

                j.getChild().getJoints().draw();
            }
        }
    }

    public String debug() {
        return "====== New Joints Debug======\nBody" + debug(1);
    }

    public String debug(int level) {
        String result = "";
        for (Joint j : _joints) {
            if (j.getChild() != null) {

                result += "\n";
                for (int ii = 0; ii < level; ii++) {
                    result += "----";
                }
                result += "(" + j.GridLoc + ") " + j.getChild().getFunction();
                result += j.getChild().getJoints().debug(level + 1);
                result = result.replace("[]", "");
            }
        }
        return result;
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