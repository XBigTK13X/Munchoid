package game.creatures;

public class Joint {
    private BodyPart _child;
    public final int GridLoc;

    public Joint(int gridLoc) {
        GridLoc = gridLoc;
    }

    public void setChild(BodyPart child) {
        _child = child;
    }

    public BodyPart getChild() {
        return _child;
    }
}
