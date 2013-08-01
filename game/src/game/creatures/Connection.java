package game.creatures;

import sps.core.Point2;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    public final Point2 Position;
    public final int GridLoc;
    public final int MaxChildren;

    private List<BodyPart> _children;
    private List<Integer> _childLocs;

    public Connection(int x, int y, int gridLoc, int maxChildren) {
        MaxChildren = maxChildren;
        Position = new Point2(x, y);
        GridLoc = gridLoc;
        _children = new ArrayList<BodyPart>();
        _childLocs = new ArrayList<Integer>();
    }

    public boolean hasRoomForChildren() {
        return _children.size() < MaxChildren;
    }

    public void addChild(BodyPart child) {
        _children.add(child);
        _childLocs.add(child.getConnections().getOpenJoint().GridLoc);
    }

    public Point2 getOrigin(BodyPart part) {
        for (int ii = 0; ii < _children.size(); ii++) {
            if (_children.get(ii) == part) {
                return new Point2(GridLoc, _childLocs.get(ii));
            }
        }
        throw new RuntimeException("No child loc association was found");
    }
}
