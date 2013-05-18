package game.arena;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.part.Common;
import game.creatures.part.Design;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.bridge.SpriteTypes;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.util.Screen;
import sps.util.SpriteMaker;

import java.util.List;

public class Arrow extends Entity {
    private static final float __radius = Screen.height(6);
    private static final float __rad2TargetDistance = (float) Math.pow(Screen.height(25), 2);
    private Player _owner;
    private Point2 _targetLoc = new Point2(0, 0);
    private Entity _closest;

    private Sprite _target;

    public Arrow(Player owner) {
        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, new Point2(0, 0), SpriteTypes.get("Arrow"), EntityTypes.get("Arrow"), DrawDepths.get("Arrow"));
        _owner = owner;

        int[][] base = new int[(int) GameConfig.MaxBodyPartSize.X][(int) GameConfig.MaxBodyPartSize.Y];
        float radius2 = 8 * (base.length + base[0].length);
        float innerRadius2 = .7f * radius2;
        Point2 center = new Point2(base.length / 2, base[0].length / 2);
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                float dist = HitTest.getDistanceSquare(ii, center.X, jj, center.Y);
                if (dist <= radius2 && dist >= innerRadius2) {
                    base[ii][jj] = Design.BaseColor;
                }
            }
        }
        Common.trim(base);
        Color[][] colorBase = new Color[base.length][base[0].length];
        for (int ii = 0; ii < colorBase.length; ii++) {
            for (int jj = 0; jj < colorBase[0].length; jj++) {
                if (base[ii][jj] == Design.BaseColor) {
                    colorBase[ii][jj] = Color.WHITE;
                }
            }
        }
        _target = SpriteMaker.get().fromColors(colorBase);
    }

    @Override
    public void update() {
        List<Entity> catchables = EntityManager.get().getEntities(EntityTypes.get("Catchable"));
        if (catchables.size() > 0) {
            _closest = catchables.get(0);
            float dist2 = Float.MAX_VALUE;
            for (Entity catchable : catchables) {
                if (HitTest.getDistanceSquare(this, catchable) < dist2) {
                    dist2 = HitTest.getDistanceSquare(this, catchable);
                    _closest = catchable;
                }
            }
            double rads = Math.atan2(getLocation().Y - _closest.getLocation().Y, getLocation().X - _closest.getLocation().X);
            _graphic.setRotation(90 + (int) (rads * 180 / Math.PI));
            double x = Math.cos(rads) * -__radius + _owner.getLocation().X;
            double y = Math.sin(rads) * -__radius + _owner.getLocation().Y;
            _targetLoc.reset((float) x, (float) y);
            _target.setPosition(_closest.getLocation().X - _target.getWidth() / 2, _closest.getLocation().Y - _target.getHeight() / 2);
            setLocation(_targetLoc);
        }
        if (HitTest.getDistanceSquare(_owner, _closest) <= __rad2TargetDistance) {
            hide();
        }
        else {
            show();
        }
    }

    @Override
    public void draw() {
        if (_isOnBoard) {
            _graphic.draw();
        }
        else {
            //Renderer.get().draw(_target);
        }
    }
}
