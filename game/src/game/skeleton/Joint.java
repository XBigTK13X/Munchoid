package game.skeleton;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.BodyPart;
import sps.entities.HitTest;
import sps.display.Window;
import sps.draw.SpriteMaker;

import java.util.LinkedList;
import java.util.List;

public class Joint {
    private BodyPart _part;

    private float _width = 10;
    private float _height = 10;
    private Sprite sprite;
    public float X;
    public float Y;
    public float Degrees;
    private List<Joint> _outbound;

    public Joint(float x, float y) {
        _outbound = new LinkedList<Joint>();
        X = x;
        Y = y;
        Color color = new Color(Color.WHITE);
        color.a = .5f;
        sprite = SpriteMaker.get().pixel(color);

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
        sprite.setSize(_width, _height);
        sprite.setRotation(Degrees);
        sprite.setPosition(X, Y);
        Window.get().draw(sprite);
        if (_outbound.size() > 0) {
            for (Joint out : _outbound) {
                Degrees = (float) (180 * Math.atan2(out.Y - Y, out.X - X) / Math.PI) - 90;
                float dist = HitTest.getDistance(X, Y, out.X, out.Y);
                int boneWidth = 3;
                sprite.setSize(boneWidth, dist);
                sprite.setRotation(Degrees);
                sprite.setPosition(X + _width / 2 - boneWidth / 2, Y + _height / 2 - boneWidth / 2);
                Window.get().draw(sprite);
            }
        }
    }
}
