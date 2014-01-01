package game.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.dev.DevConfig;
import game.pregame.PreloadMainMenu;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.display.Screen;
import sps.display.Window;
import sps.states.State;
import sps.states.StateManager;
import sps.ui.MultiText;

import java.text.NumberFormat;
import java.util.Locale;

public class EndGame implements State {
    private Sprite _background;
    private MultiText _results;

    @Override
    public void create() {
        _background = BackgroundCache.getRandom();
        _results = new MultiText(Screen.pos(10, 10), 20, Color.BLACK.newAlpha(.4f), (int) Screen.width(80), (int) Screen.height(80));
        _results.setFont("default", 40);
        _results.setDoubleSpaced(true);

        _results.add("The people have granted you the title of " + getTitle());

        if (WorldScore.TournamentWins == 0) {
            _results.add("Your weak Munchoids solved simple causes of death.");
            _results.add("However, not a single tournament was won.");
            _results.add("Almost everyone in your region has succumbed to the epidemic.");
            _results.add("You find " + getMoney() + " among the wasteland that was once your home.");
            _results.add("Better luck next universe!");
        }
        else if (WorldScore.TournamentWins == GameConfig.NumberOfTournaments) {
            _results.add("You championed each tournament on this planet.");
            _results.add("The resulting Munchoids were able to solve most causes of death.");
            _results.add("Everyone in the region heralds you as their savior.");
            _results.add("Few faced their mortality thanks to your work.");
            _results.add("The people provided you with a cash reward of " + getMoney() + ".");
        }
        else {
            _results.add("You are welcomed home after winning " + WorldScore.TournamentWins + " of the " + GameConfig.NumberOfTournaments + " tournaments.");
            _results.add("Thanks to your efforts some severe causes of death were solved.");
            _results.add("With a larger population available to face the epidemic the region survived.");
            _results.add("Many have died, but more remain to carry on and rebuild.");
            _results.add("Your people chose to grant you a gift of" + getMoney() + ".");
        }
        _results.add("Press " + Commands.get("Confirm") + " to return to the main menu and play in a parallel universe.");
    }

    private static final NumberFormat dollars = NumberFormat.getCurrencyInstance(Locale.US);

    private String getMoney() {
        if (ArenaScore.get().total() <= 0) {
            return "no money";
        }
        return dollars.format(WorldScore.ArenaTotal);
    }

    private String getTitle() {
        switch (WorldScore.TournamentWins) {
            case 0:
                return "\"Complete Failure\"";
            case 1:
                return "\"Participatory Gold-Starred\"";
            case 2:
                return "\"Middle Child of Humanity\"";
            case 3:
                return "\"Average Hero\"";
            case 4:
                return "\"Bringer of Hope\"";
            case 5:
                return "\"Ultimate Savior\"";
            default:
                return "\"Breaker of Games\"";
        }
    }


    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
        _results.draw();
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || DevConfig.EndToEndStateLoadTest || DevConfig.BotEnabled) {
            StateManager.reset().push(new PreloadMainMenu());
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
