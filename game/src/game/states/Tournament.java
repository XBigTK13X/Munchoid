package game.states;

import game.arena.Player;
import game.tournament.Bracket;
import sps.states.State;

public class Tournament implements State {

    private static final int __matches = 2;

    private Bracket _bracket;

    private Player _player;

    public Tournament(Player player) {
        _player = player;
    }

    @Override
    public void create() {
        _bracket = new Bracket(_player, __matches);
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
        //This should only be called after popping
        //the merge results state after a player victory
        if (_bracket != null) {
            _bracket.removeLastOpponent();
        }
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
