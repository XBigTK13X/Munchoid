package game.test;

import game.core.EndGame;
import game.core.InputWrapper;
import game.core.Score;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class EndGameTest implements State {
    @Override
    public void create() {
        TextPool.get().write(Commands.get("Confirm") + " to win all\n" + Commands.get("Pop") + " to lose all\n" + Commands.get("Push") + " to lose some", Screen.pos(30, 30));
    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            Score.get().set(0, 0, 0, 0, 0, 0, 0, 5, 0);
            StateManager.get().push(new EndGame());
        }
        if (InputWrapper.pop()) {
            Score.get().set(0, 0, 0, 0, 0, 0, 0, 0, 5);
            StateManager.get().push(new EndGame());
        }
        if (InputWrapper.push()) {
            Score.get().set(0, 0, 0, 0, 0, 0, 0, 2, 3);
            StateManager.get().push(new EndGame());
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
    public void pause() {

    }
}