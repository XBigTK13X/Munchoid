package game.creatures;

import sps.core.Logger;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Connections {
    private class Connection {
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
    public boolean addChildIfPossible(BodyPart child) {
        if (_connections.size() == 0) {
            _connections.add(new Connection(0, 0, child.getFunction().MaxChildren));
            return true;
        }
        else {
            Logger.info("Adding: " + child + ", " + child.getFunction() + ", " + _connections.size() + "," + _connections.get(0).MaxChildren);
            Connection c = _connections.get(RNG.next(_connections.size()));
            if (c.hasRoomForChildren()) {
                c.addChild(child);
                _children.add(child);
                return true;
            }
            return false;
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
}