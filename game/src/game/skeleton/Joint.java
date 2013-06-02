package game.skeleton;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.HitTest;
import sps.graphics.Renderer;
import sps.util.SpriteMaker;

public class Joint {
    private float _width = 10;
    private float _height = 10;
    private Sprite sprite;
    public float X;
    public float Y;
    public float Degrees;
    private Joint _outbound;
    public Joint Inbound;

    private float rangeDegrees = 10;

    private static int id;
    private int _id;

    public Joint(float x, float y) {
        _id = id++;
        X = x;
        Y = y;
        sprite = SpriteMaker.get().pixel(Color.WHITE);
    }

    public void setOutbound(Joint _outbound) {
        this._outbound = _outbound;

    }

    public Joint getOutbound() {
        return _outbound;
    }

    public void update() {
        float delt = .05f;
        Point2 m = RNG.point(-10, 10, -10, 10);
        X += m.X * delt;
        Y += m.Y * delt;
        if (_outbound != null) {
            Degrees = (float) (180 * Math.atan2(this._outbound.Y - Y, this._outbound.X - X) / Math.PI) - 90;
        }
    }

    public void draw() {
        sprite.setSize(_width, _height);
        sprite.setRotation(Degrees);
        sprite.setPosition(X, Y);
        Renderer.get().draw(sprite);
        if (_outbound != null) {
            float dist = HitTest.getDistance(X, Y, _outbound.X, _outbound.Y);
            int boneWidth = 3;
            sprite.setSize(boneWidth, dist);
            sprite.setRotation(Degrees);
            sprite.setPosition(X + _width / 2 - boneWidth / 2, Y + _height / 2 - boneWidth / 2);
            Renderer.get().draw(sprite);
        }
    }
}
