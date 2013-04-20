package game.states;

import game.GameConfig;
import game.arena.Player;
import game.tournament.Bracket;
import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.text.TextPool;
import sps.util.Screen;

public class Tournament implements State {

    private Bracket _bracket;
    private Player _player;

    private int _boutNumber = 1;

    public Tournament(Player player) {
        _player = player;
    }

    @Override
    public void create() {
        _bracket = new Bracket(_player, GameConfig.TournamentMatches);
        TextPool.get().write("SPACE for bout " + _boutNumber + " of " + GameConfig.TournamentMatches, Screen.pos(5, 50));
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            _boutNumber++;
            _bracket.runNextMatch();
        }
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
