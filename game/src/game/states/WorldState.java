package game.states;

import game.Shared;
import game.creatures.Creature;
import sps.entities.EntityManager;
import sps.states.State;
import sps.states.StateManager;

public class WorldState implements State {
    @Override
    public void create() {
        if (Shared.get().playerCreature() == null) {
            Shared.get().setPlayerCreature(new Creature(false));
        }
        StateManager.get().push(new BattleState());
    }

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
        return "World";
    }

    @Override
    public void resize(int width, int height) {
    }
}
