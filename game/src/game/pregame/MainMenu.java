package game.pregame;

import game.GameConfig;
import game.InputWrapper;
import game.Score;
import game.population.PopulationOverview;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;

public class MainMenu implements State {
    @Override
    public void create() {
        TextPool.get().write("Munchoid", Screen.pos(20, 70));
        TextPool.get().write("Press SPACE to begin.", Screen.pos(10, 60));
        StateManager.clearTimes();
        Score.reset();
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            StateManager.get().push(new PopulationOverview());
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
