package sps.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.graphics.Renderer;
import sps.util.Colors;
import sps.util.MathHelper;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class Particle2 extends PEComponent {
    public static final int DefaultLife = 100;

    public float Height = Screen.width(1);
    public float Width = Screen.width(1);

    private float _life = DefaultLife;
    private Color _color = Color.WHITE;

    public float MoveSpeed = 5f;

    public Entity Entity;
    public final Point2 Position = new Point2(0, 0);
    public final Point2 Origin = new Point2(0, 0);
    public double Angle;
    public float Radius;
    public boolean Toggle;

    private Sprite _sprite;

    private ParticleBehavior _behavior;

    public Particle2() {
        IsActive = false;
    }


    public void draw() {
        if (IsActive) {
            _sprite.setSize(Width, Height);
            _sprite.setColor(_color);
            _sprite.setRotation((float) (Angle * 180 / Math.PI));
            _sprite.setPosition(Position.X, Position.Y);
            Renderer.get().draw(_sprite);
        }
    }

    public void reset(ParticleBehavior behavior, Point2 position, Color baseColor) {
        init(behavior, position, null, baseColor);
    }

    public void reset(ParticleBehavior behavior, Entity entity, Color baseColor) {
        init(behavior, null, entity, baseColor);
    }

    private void init(ParticleBehavior behavior, Point2 position, Entity entity, Color baseColor) {

        _sprite = SpriteMaker.get().fromColors(Colors.genArr((int) Width, (int) Height, baseColor));
        _behavior = behavior;
        if (position != null) {
            Origin.reset(position.X, position.Y);
            Position.reset(position.X, position.Y);
        }
        if (entity != null) {
            Entity = entity;
            Origin.reset(Entity.getLocation().PosX, Entity.getLocation().PosY);
            Position.reset(Entity.getLocation().PosX, Entity.getLocation().PosY);
        }
        if (baseColor != null) {
            _color = darken(baseColor, (RNG.next(10, 50)) / 100f);
        }
        else {
            _color = new Color(RNG.next(60, 190) / 255f, RNG.next(60, 190) / 255f, RNG.next(60, 190) / 255f, 1f);
        }
        Angle = RNG.angle();
        IsActive = true;
        Radius = 0;
        setMoveSpeed();
        _behavior.setup(this);
    }

    private void setMoveSpeed() {
        MoveSpeed = 15f - (10f * ((Height + Width) / 30f));
    }

    private static Color darken(Color inColor, float inAmount) {
        return new Color(MathHelper.clamp((inColor.r) * 255 - 255 * inAmount, 0, 255), MathHelper.clamp((inColor.g) * 255 - 255 * inAmount, 0, 255), MathHelper.clamp((inColor.b) * 255 - 255 * inAmount, 0, 255), 255);
    }

    public void setSize(float height, float width) {
        Height = height;
        Width = width;
        setMoveSpeed();
    }

    public void update() {
        _life *= .85f;
        _color.a *= .999f;
        if ((_life <= .001 && Entity == null) || (Entity != null && !Entity.isActive())) {
            IsActive = false;
        }
        _behavior.update(this);
    }

    public void clear() {
        _color.a = 0;
        _life = 0;
        IsActive = false;
    }
}
