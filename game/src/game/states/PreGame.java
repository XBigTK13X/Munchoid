package game.states;

import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;

public class PreGame implements State {
    @Override
    public void create() {

    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            StateManager.get().push(new Tournament());
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
}
