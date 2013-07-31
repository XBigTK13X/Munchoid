package game.creatures;

import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Connections {
    private List<BodyPart> _children;
    private List<Connection> _connections;

    public Connections() {
        _children = new ArrayList<BodyPart>();
        _connections = new ArrayList<Connection>();
    }

    public List<BodyPart> getChildren() {
        return _children;
    }


    //TODO Have a separate method that informs as to whether or not children will fit ahead of time
    public void addChild(BodyPart child) {
        Connection c = null;
        while (c == null || !c.hasRoomForChildren()) {
            c = _connections.get(RNG.next(_connections.size()));
            c.addChild(child);
            _children.add(child);
        }
    }

    public boolean hasSpace() {
        for (Connection c : _connections) {
            if (c.hasRoomForChildren()) {
                return true;
            }
        }
        return false;
    }

    public void addConnection(Connection connection) {
        _connections.add(connection);
    }
}