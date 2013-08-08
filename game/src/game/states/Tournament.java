package game.states;

import game.GameConfig;
import game.InputWrapper;
import game.arena.Player;
import game.tournament.Bracket;
import sps.display.Screen;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

public class Tournament implements State {

    private Bracket _bracket;
    private Player _player;

    private int _boutNumber = 1;

    Text _entranceInfo;

    public Tournament(Player player) {
        _player = player;
    }

    @Override
    public void create() {
        _bracket = new Bracket(_player, GameConfig.TournamentMatches);
        _entranceInfo = TextPool.get().write(boutMessage(), Screen.pos(5, 50));
    }

    private String boutMessage() {
        return "SPACE for bout " + _boutNumber + " of " + GameConfig.TournamentMatches;
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            _boutNumber++;
            _entranceInfo.setMessage(boutMessage());
            // Run all of the EvE matches and Byes, then load the next PvE match
            while (!_bracket.runNextMatch()) {
            }
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

    @Override
    public void pause() {
    }
}
