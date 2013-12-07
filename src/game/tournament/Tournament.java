package game.tournament;

import game.DevConfig;
import game.GameConfig;
import game.InputWrapper;
import game.arena.Player;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

public class Tournament implements State {

    private Matches _matches;
    private Player _player;

    private int _boutNumber = 1;

    Text _entranceInfo;

    public Tournament(Player player) {
        _player = player;
    }

    @Override
    public void create() {
        _matches = new Matches(_player, GameConfig.TournamentMatches);
        _entranceInfo = TextPool.get().write(boutMessage(), Screen.pos(5, 50));
    }

    private String boutMessage() {
        String message = "Your arena is empty and the strongest from other arenas have gathered!";
        message += "\nBeat all of them to gain control of the strongest Munchoid.";
        message += "\n\nPress " + Commands.get("Confirm") + " to start tournament match " + _boutNumber + " of " + GameConfig.TournamentMatches;
        return message;
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            _boutNumber++;
            _entranceInfo.setMessage(boutMessage());
            // Run all of the EvE matches and Byes, then load the next PvE match
            _matches.simulateCpuOnlyRounds();
            _matches.beginPlayerMatch();
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        //This should only be called after popping
        //the merge results state after a player victory
        if (_matches != null) {
            _matches.removeLastOpponent();
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
