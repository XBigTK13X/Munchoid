package game.core;

import game.dev.DevConfig;
import game.pregame.MainMenu;
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
        TextPool.get().write("You were able to win " + _wins + " out of " + GameConfig.NumberOfTournaments + " tournaments", Screen.pos(10, 60));
        TextPool.get().write(Score.get().message(), Screen.pos(10, 40));
        TextPool.get().write("Press " + Commands.get("Confirm") + " to play in a parallel universe.", Screen.pos(10, 20));
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
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
    public void pause() {
    }
}
