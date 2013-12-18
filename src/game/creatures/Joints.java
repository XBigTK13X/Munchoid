package game.creatures;

import game.DevConfig;
import game.GameConfig;
import sps.color.Color;
import sps.display.DrawApiCall;
import sps.display.Window;

import java.util.ArrayList;
import java.util.List;

public class Joints {
    private List<Joint> _joints;

    public Joints() {
        _joints = new ArrayList<Joint>();
    }

    public void add(Joint joint) {
        _joints.add(joint);
    }

    public List<Joint> getAll() {
        return _joints;
    }

    //TODO Hash lookup
    public Joint get(int gridLoc) {
        for (Joint j : _joints) {
            if (j.GridLoc == gridLoc) {
                return j;
            }
        }
        return null;
    }

    public void draw() {
        if (DevConfig.DrawSkeleton || GameConfig.OptDrawBones) {
            for (Joint j : _joints) {
                if (j.getChild() != null) {
                    if (DevConfig.DrawSkeleton) {
                        int radBG = 7;
                        int radFG = radBG - 2;

                        Color bg = Color.BLACK;
                        Color fg = Color.WHITE;
                        Color lbg = Color.WHITE;
                        Color lfg = Color.BLACK;
                        int lO = 2;

                        Window.get().schedule(new DrawApiCall(bg, j.getGlobalCenter().X, j.getGlobalCenter().Y, radBG));
                        Window.get().schedule(new DrawApiCall(fg, j.getGlobalCenter().X, j.getGlobalCenter().Y, radFG));

                        Window.get().schedule(new DrawApiCall(lbg, j.getGlobalCenter().X - lO, j.getGlobalCenter().Y - lO, j.getChildGlobalCenter().X - lO, j.getChildGlobalCenter().Y - lO));
                        Window.get().schedule(new DrawApiCall(lfg, j.getGlobalCenter().X, j.getGlobalCenter().Y, j.getChildGlobalCenter().X, j.getChildGlobalCenter().Y));
                        Window.get().schedule(new DrawApiCall(lbg, j.getGlobalCenter().X + lO, j.getGlobalCenter().Y + lO, j.getChildGlobalCenter().X + lO, j.getChildGlobalCenter().Y + lO));

                        Window.get().schedule(new DrawApiCall(bg, j.getChildGlobalCenter().X, j.getChildGlobalCenter().Y, radBG));
                        Window.get().schedule(new DrawApiCall(fg, j.getChildGlobalCenter().X, j.getChildGlobalCenter().Y, radFG));

                        j.getChild().getJoints().draw();
                    }
                    else if (GameConfig.OptDrawBones) {
                        j.draw();
                    }
                }
            }
        }
    }

    public String debug() {
        return "====== New Joints Debug======\nCore" + debug(1);
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
}
