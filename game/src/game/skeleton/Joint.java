package game.skeleton;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;
import sps.graphics.Renderer;
import sps.util.SpriteMaker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Joint {
    private float _width = 10;
    private float _height = 10;
    private Sprite sprite;
    public float X;
    public float Y;
    public float Degrees;
    private List<Joint> _outbound;
    private Joint _inbound;

    private float rangeDegrees = 10;

    private static int id;
    private int _id;

    public Joint(float x, float y) {
        _outbound = new LinkedList<Joint>();
        _id = id++;
        X = x;
        Y = y;
        sprite = SpriteMaker.get().pixel(Color.WHITE);
    }

    public void addOutbound(Joint outbound) {
        _outbound.add(outbound);
    }

    public List<Joint> getOutbounds() {
        return _outbound;
    }

    public void setInbound(Joint inbound){
        _inbound = inbound;
    }

    public void update() {
        float delt = .05f;
        Point2 m = RNG.point(-10, 10, -10, 10);
        X += m.X * delt;
        Y += m.Y * delt;
        if (_outbound.size() > 0) {
            Degrees = (float) (180 * Math.atan2(_outbound.get(0).Y - Y, _outbound.get(0).X - X) / Math.PI) - 90;
        }
    }

    public void draw() {
        sprite.setSize(_width, _height);
        sprite.setRotation(Degrees);
        sprite.setPosition(X, Y);
        Renderer.get().draw(sprite);
        if (_outbound.size() > 0) {
            float dist = HitTest.getDistance(X, Y, _outbound.get(0).X, _outbound.get(0).Y);
            int boneWidth = 3;
            sprite.setSize(boneWidth, dist);
            sprite.setRotation(Degrees);
            sprite.setPosition(X + _width / 2 - boneWidth / 2, Y + _height / 2 - boneWidth / 2);
            Renderer.get().draw(sprite);
        }
    }
}
