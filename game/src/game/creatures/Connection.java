package game.creatures;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    public final int MaxChildren;
    public final int X;
    public final int Y;

    private List<BodyPart> _children;

    public Connection(int x, int y, int maxChildren) {
        MaxChildren = maxChildren;
        X = x;
        Y = y;
        _children = new ArrayList<BodyPart>();
    }

    public boolean hasRoomForChildren() {
        return _children.size() < MaxChildren;
    }

    public void addChild(BodyPart child) {
        _children.add(child);
    }
}
