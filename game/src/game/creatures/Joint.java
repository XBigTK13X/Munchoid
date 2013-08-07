package game.creatures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Grid;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.entities.HitTest;
import sps.util.Bounds;

public class Joint {
    private BodyPart _child;
    public final int GridLoc;
    private BodyPart _owner;

    private static Sprite _bone;
    private static final float __boneThickness = Screen.width(1) / 2;
    private float _childDist;
    private float _childRot;
    private float _lastChildScale = 1f;
    private float _lastOwnerScale = 1f;

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

    public void draw() {
        if (_bone == null) {
            _bone = SpriteMaker.get().pixel(Color.WHITE);
            _bone.setSize(__boneThickness, 1);
        }
        if (_child != null && _child.isAlive() && _owner.isAlive()) {
            if (_childDist == 0 || _lastOwnerScale != _owner.getScale() || _lastChildScale != _child.getScale()) {
                Bounds b = Bounds.fromPoints(getGlobalCenter().X, getGlobalCenter().Y, _child.getGlobalPosition().X + _child.getWidth() / 2, _child.getGlobalPosition().Y + _child.getHeight() / 2);
                _childDist = HitTest.getDistance(b.X, b.Y, b.X2, b.Y2);
                _childRot = 90 + (int) (180 * Math.atan2(b.Y - b.Y2, b.X - b.X2) / Math.PI);
                _lastChildScale = _child.getScale();
                _lastOwnerScale = _owner.getScale();
            }
            _bone.setSize(__boneThickness, _childDist);
            _bone.setPosition(getGlobalCenter().X, getGlobalCenter().Y);
            _bone.setRotation(_childRot);
            Window.get().draw(_bone);
        }
    }
}
