package game.creatures;

import game.creatures.style.Grid;
import sps.core.Point2;

public class Joint {
    private BodyPart _child;
    public final int GridLoc;
    private BodyPart _owner;

    public Joint(int gridLoc, BodyPart owner) {
        GridLoc = gridLoc;
        _owner = owner;
    }

    public void setChild(BodyPart child) {
        _child = child;
        //TODO Attach and base rotation on that
    }

    public BodyPart getChild() {
        return _child;
    }

    public Point2 getGlobalCenter() {
        return getLocalCenter().add(_owner.getGlobalPosition());
    }

    public Point2 getLocalCenter() {
        return Grid.centerOf(GridLoc, _owner);
    }

    public Point2 getChildGlobalCenter() {
        return _child.getJoints().get(_child.getFunction().LocAttach).getGlobalCenter();
    }
}
