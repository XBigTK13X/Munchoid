package game.states;

import sps.core.Point2;
import sps.core.RNG;
import sps.entities.EntityManager;
import sps.states.State;
import sps.text.TextPool;

public class BattleState implements State {
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
        TextPool.get().write("WHALSD", new Point2(RNG.next(0, 1000), RNG.next(0, 1000)));
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
