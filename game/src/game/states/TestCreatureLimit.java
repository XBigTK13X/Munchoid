package game.states;

import game.GameConfig;
import game.creatures.Atom;
import game.creatures.Creature;
import sps.core.Logger;
import sps.entities.EntityManager;
import sps.states.State;

public class TestCreatureLimit implements State {

    @Override
    public void create() {
        //Test the limits of full-size creature generation
        int creatureCount = 0;
        while (creatureCount < GameConfig.CreatureLimit) {
            Logger.info("Atoms: " + Atom.count + ", Creatures: " + creatureCount++);
            EntityManager.get().addEntity(new Creature());
        }
    }

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
