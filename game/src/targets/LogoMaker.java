package targets;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.InputWrapper;
import sps.core.Logger;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
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

    private void regen(){
        Color[][] base = ProcTextures.perlin(800, 800, Color.BLACK, Colors.rgb(155, 100, 155), 8);

        ProcTextures.blur(base, 2);

        _bg = SpriteMaker.get().fromColors(base);
        _bg.setPosition(50, 50);
    }

    @Override
    public void resize(int i, int i2) {
    }

    @Override
    public void render() {
        Input.get().update();
        if(InputWrapper.confirm()){
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
