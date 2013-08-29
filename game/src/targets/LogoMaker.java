package targets;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.InputWrapper;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.entities.HitTest;
import sps.io.Input;

public class LogoMaker implements ApplicationListener {
    private static DummyApp _context;

    public static void main(String[] args) {
        _context = new DummyApp(new LogoMaker());
    }

    private Sprite _bg;

    @Override
    public void create() {
        _context.setup();
        regen();
    }

    private void regen() {
        Color[][] base = ProcTextures.perlin(800, 800, Color.BLACK, Colors.rgb(155, 100, 155), 8);

        ProcTextures.blur(base, 2);

        int rad1 = 30;
        int rad2 = 100;
        int rad3 = 150;
        Point2 center = new Point2(400, 400);
        for (int ii = 0; ii < base.length; ii++) {
            for (int jj = 0; jj < base[0].length; jj++) {
                Point2 t = new Point2(ii, jj);
                float dist = HitTest.getDistance(center, t);
                if (dist < rad1) {
                    base[ii][jj] = Color.BLACK;
                }
                else if (dist > rad2 && dist < rad3) {
                    base[ii][jj] = base[ii][jj];
                }
                else {
                    base[ii][jj] = null;
                }
            }
        }

        Outline.single(base, Color.WHITE, 5);

        if (_bg != null) {
            _bg.getTexture().dispose();
        }

        _bg = SpriteMaker.get().fromColors(base);
        _bg.setPosition(50, 50);
    }

    @Override
    public void resize(int i, int i2) {
    }

    @Override
    public void render() {
        Input.get().update();
        if (InputWrapper.confirm()) {
            regen();
        }
        Window.get().begin();
        Window.get().draw(_bg);
        Window.get().end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
