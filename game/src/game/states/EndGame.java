package game.states;

import game.GameConfig;
import game.InputWrapper;
import game.MetaData;
import game.Score;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class EndGame implements State {
    private int _wins;

    public EndGame(int wins) {
        _wins = wins;
    }

    @Override
    public void create() {
        TextPool.get().write("The games are complete on this world!", Screen.pos(10, 80));
        TextPool.get().write("You were able to win " + _wins + (_wins == 1 ? " tournament" : " tournaments"), Screen.pos(10, 60));
        TextPool.get().write(Score.get().message(), Screen.pos(10, 40));
        TextPool.get().write("Press " + Commands.get("Confirm") + " to play in a parallel universe.", Screen.pos(10, 20));
        MetaData.printWin();
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
