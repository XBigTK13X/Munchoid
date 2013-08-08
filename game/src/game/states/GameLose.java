package game.states;

import game.GameConfig;
import game.InputWrapper;
import game.MetaData;
import game.Score;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class GameLose implements State {
    @Override
    public void create() {
        TextPool.get().write("You Lose", Screen.pos(10, 80));
        TextPool.get().write(Score.get().message(), Screen.pos(10, 60));
        TextPool.get().write("SPACE to restart", Screen.pos(10, 40));
        MetaData.printLose();
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
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
