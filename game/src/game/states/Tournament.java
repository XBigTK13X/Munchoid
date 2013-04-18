package game.states;

import game.tournament.Bracket;
import sps.states.State;

public class Tournament implements State {

    private static final int __matches = 2;

    private Bracket _bracket;

    @Override
    public void create() {
        _bracket = new Bracket(__matches);
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        _bracket.runNextMatch();
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
