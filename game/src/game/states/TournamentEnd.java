package game.states;

import sps.states.State;
import sps.states.StateManager;

public class TournamentEnd implements State {
    boolean _win;

    public TournamentEnd(boolean win) {
        _win = win;
    }

    @Override
    public void create() {
        StateManager.get().rollBackTo(PopulationOverview.class);
        PopulationOverview state = (PopulationOverview) StateManager.get().current();
        if (_win) {
            state.addWin();
        }
        else {
            state.addLoss();
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
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
        return "TournamentEnd";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
