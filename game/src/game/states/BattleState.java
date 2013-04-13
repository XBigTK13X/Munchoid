package game.states;

import sps.entities.EntityManager;
import sps.states.State;

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
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
    }

    @Override
    public String getName() {
        return "Battle State";
    }

    @Override
    public void resize(int width, int height) {
    }
}
