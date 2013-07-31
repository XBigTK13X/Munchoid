package game.creatures;

import sps.core.Point2;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    public final Point2 Position;
    public final int GridLoc;
    public final int MaxChildren;

    private List<BodyPart> _children;

    public Connection(int x, int y, int gridLoc, int maxChildren) {
        MaxChildren = maxChildren;
        Position = new Point2(x, y);
        GridLoc = gridLoc;
        _children = new ArrayList<BodyPart>();
    }

    public boolean hasRoomForChildren() {
        return _children.size() < MaxChildren;
    }

    public void addChild(BodyPart child) {
        _children.add(child);
    }
}
