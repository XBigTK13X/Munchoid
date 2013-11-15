package game.test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.BackgroundMaker;
import game.InputWrapper;
import sps.bridge.DrawDepths;
import sps.display.Window;
import sps.states.State;

public class BackgroundGenerationTest implements State {
    private Sprite _bg;

    @Override
    public void create() {
        _bg = BackgroundMaker.printedCircuitBoard();
    }

    @Override
    public void draw() {
        Window.get().schedule(_bg, DrawDepths.get("GameBackground"));
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            _bg = BackgroundMaker.printedCircuitBoard();
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
