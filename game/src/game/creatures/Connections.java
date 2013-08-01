package game.creatures;

import sps.core.Point2;
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

    public void addChild(BodyPart child, Connection c) {
        c.addChild(child);
        _children.add(child);
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

    public Connection getOpenJoint() {
        int maxTries = 100;
        while (maxTries-- >= 0) {
            Connection c = _connections.get(RNG.next(_connections.size()));
            if (c.hasRoomForChildren()) {
                return c;
            }
        }
        throw new RuntimeException("No open joints found.");
    }

    public Point2 getGridConnectionTo(BodyPart part) {
        for (Connection c : _connections) {
            Point2 origin = c.getOrigin(part);
            if (origin != null) {
                return origin;
            }
        }
        throw new RuntimeException("Part has no connections.");
    }
}