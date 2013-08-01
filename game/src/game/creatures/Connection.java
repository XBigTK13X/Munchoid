package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.creatures.style.BodyRules;
import sps.core.Point2;
import sps.draw.DrawAPI;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    public static class Attachment {
        public final BodyPart Part;
        public final int GridLoc;

        public Attachment(BodyPart part, int gridLoc) {
            Part = part;
            GridLoc = gridLoc;
        }
    }

    public final Point2 Position;
    public final int GridLoc;
    public final int MaxChildren;
    public Connections Container;

    private List<Attachment> _attachments;

    public Connection(int x, int y, int gridLoc, int maxChildren) {
        MaxChildren = maxChildren;
        Position = new Point2(x, y);
        GridLoc = gridLoc;
        _attachments = new ArrayList<Attachment>();

    }

    public void setContainer(Connections c) {
        Container = c;
    }

    public boolean hasRoomForChildren() {
        return _attachments.size() < MaxChildren;
    }

    public void addChild(BodyPart child) {
        _attachments.add(new Attachment(child, child.getConnections().getOpenJoint().GridLoc));
    }

    public Point2 getOrigin(BodyPart part) {
        for (int ii = 0; ii < _attachments.size(); ii++) {
            if (_attachments.get(ii).Part == part) {

                return new Point2(GridLoc, _attachments.get(ii).GridLoc);
            }
        }
        return null;
    }

    public void draw() {
        DrawAPI.get().setColor(Color.BLUE);
        Point2 jointPos = BodyRules.gridRange(GridLoc, Container.getOwner().getWidth(), Container.getOwner().getHeight());
        DrawAPI.get().circle(jointPos.X, jointPos.Y, 10);
    }
}
