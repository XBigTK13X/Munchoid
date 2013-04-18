package game.states;

import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.util.Screen;

public class GameLose implements State {
    @Override
    public void create() {
        TextPool.get().write("You Lose", Screen.pos(10, 60));
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            StateManager.reset().push(new PreGame());
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
