package game.states;

import game.GameConfig;
import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.util.Screen;

public class MainMenu implements State {
    @Override
    public void create() {
        TextPool.get().write("Munchoid", Screen.pos(20, 70));
        TextPool.get().write("Press SPACE to enter the arena.", Screen.pos(10, 60));
        StateManager.clearTimes();
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm")) || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            StateManager.get().push(new LoadArena());
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
        return "Main Menu";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
