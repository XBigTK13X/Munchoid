package game.states;

import game.GameConfig;
import game.MetaData;
import game.Score;
import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.util.Screen;

public class GameWin implements State {
    @Override
    public void create() {
        TextPool.get().write("A WINNER IS YOU", Screen.pos(10, 80));
        TextPool.get().write(Score.get().message(), Screen.pos(10, 60));
        TextPool.get().write("Press SPACE to restart", Screen.pos(10, 40));
        MetaData.printWin();
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm")) || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            StateManager.reset().push(new MainMenu());
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

    @Override
    public void pause() {
    }
}
