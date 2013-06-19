package game.states;

import game.creatures.Creature;
import sps.bridge.Commands;
import sps.core.RNG;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.util.Screen;

public class AnimationTest implements State {

    private Creature creature;

    @Override
    public void create() {
        int partCount = RNG.next(4, 8);
        creature = new Creature(partCount);
        creature.setLocation(Screen.pos(50, 50));
    }

    @Override
    public void draw() {
        creature.draw();

    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            StateManager.get().push(new AnimationTest());
        }
        if (Input.get().isActive(Commands.get("Pop"))) {
            creature.getBody().flipX(!creature.getBody().isFlipX());
        }
        creature.update();
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
