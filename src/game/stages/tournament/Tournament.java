package game.stages.tournament;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.stages.arena.Player;
import game.app.core.BackgroundCache;
import game.config.GameConfig;
import game.app.core.InputWrapper;
import game.app.dev.DevConfig;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.display.Screen;
import sps.display.Window;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

public class Tournament implements State {

    private Matches _matches;
    private Player _player;
    private Sprite _background;

    private int _boutNumber = 1;

    Text _entranceInfo;

    public Tournament(Player player) {
        _player = player;
        _background = BackgroundCache.getRandom();
    }

    @Override
    public void create() {
        _matches = new Matches(_player, GameConfig.BattlesPerTournament);
        _entranceInfo = TextPool.get().write(boutMessage(), Screen.pos(5, 50));
    }

    private String boutMessage() {
        String message = "Your arena is empty and the strongest from other arenas have gathered!";
        message += "\nBeat all of them to create a powerful Munchoid.";
        message += "\n\nPress " + Commands.get("Confirm") + " to start tournament match " + _boutNumber + " of " + GameConfig.BattlesPerTournament;
        return message;
    }

    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
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
    public void pause() {
    }
}
