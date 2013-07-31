package game.skeleton;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.BodyPart;
import sps.display.DrawAPICall;
import sps.display.Window;
import sps.draw.DrawAPI;

import java.util.LinkedList;
import java.util.List;

public class Joint {
    private BodyPart _part;

    private static final float __radius = 5;
    public float X;
    public float Y;
    private List<Joint> _outbound;

    public Joint(float x, float y) {
        _outbound = new LinkedList<Joint>();
        X = x;
        Y = y;
        Color color = new Color(Color.GREEN);
        DrawAPI.get().setColor(color);
    }

    public Joint(BodyPart part) {
        this(part.getGlobalPosition().X, part.getGlobalPosition().Y);
        _part = part;
    }

    public void addOutbound(Joint outbound) {
        _outbound.add(outbound);
    }

    public List<Joint> getOutbounds() {
        return _outbound;
    }

    public void update() {
        X = _part.getGlobalPosition().X + _part.getWidth() / 2;
        Y = _part.getGlobalPosition().Y + _part.getHeight() / 2;
    }

    public void draw() {
        Window.get().schedule(new DrawAPICall(Color.GREEN,X,Y,__radius));
        if (_outbound.size() > 0) {
            for (Joint out : _outbound) {
                Window.get().schedule(new DrawAPICall(Color.BLUE,X,Y,out.X,out.Y));
            }
        }
    }
}
