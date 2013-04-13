package game.states;

import sps.core.Point2;
import sps.core.RNG;
import sps.entities.EntityManager;
import sps.graphics.Renderer;
import sps.states.State;
import sps.text.TextPool;

public class GameplayState implements State {
    @Override
    public void draw() {
        EntityManager.get().draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        TextPool.get().write("The game is running :)", new Point2(RNG.next(0, 100), Renderer.get().VirtualHeight - RNG.next(0, 1000)));
        EntityManager.reset();
        EntityManager.get().loadContent();
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
    }

    @Override
    public String getName() {
        return "Gameplay State";
    }

    @Override
    public void resize(int width, int height) {
    }
}
