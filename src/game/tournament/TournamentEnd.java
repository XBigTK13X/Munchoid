package game.tournament;

import game.population.PopulationOverview;
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
        state.tournamentResult(_win);
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
    public void pause() {
    }
}
