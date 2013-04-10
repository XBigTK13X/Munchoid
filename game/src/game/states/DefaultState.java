package game.states;

import sps.core.Point2;
import sps.graphics.Renderer;
import sps.states.State;
import sps.text.TextPool;

public class DefaultState implements State {
    @Override
    public void draw() {
    }

    @Override
    public void update() {
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        TextPool.get().write("The game is running :)",new Point2(100, Renderer.get().VirtualHeight-120));
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
}
