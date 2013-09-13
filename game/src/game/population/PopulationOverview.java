package game.population;

import game.*;
import game.arena.LoadArena;
import game.save.Persistence;
import org.apache.commons.lang3.text.WordUtils;
import sps.bridge.Commands;
import sps.core.Loader;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Markov;

import java.text.NumberFormat;

public class PopulationOverview implements State {

    private static final Markov __regionNames = Markov.get(Loader.get().data("region_name_seed.txt"), 2);

    private Population _population;

    private DiseaseMonitor _topDiseases;
    private DiseaseMonitor _bottomDiseases;

    private PopulationHUD _populationHud;
    private Text _populationCountDisplay;

    private Text _continuePrompt;

    private int _tournamentsPlayed = 0;
    private int _tournamentWins = 0;

    private String _regionName;

    @Override
    public void create() {
        _population = new Population();
        Point2 hudSize = Screen.pos(40, 70);
        Point2 hudPosition = Screen.pos(30, 15);
        _populationHud = new PopulationHUD(_population, hudSize, hudPosition);

        _topDiseases = new DiseaseMonitor(true);
        _bottomDiseases = new DiseaseMonitor(false);

        _populationCountDisplay = TextPool.get().write("", Screen.pos(30, 95));

        _regionName = __regionNames.makeWord(RNG.next(7, 10));
        _regionName = WordUtils.capitalize(_regionName);

        updateDiseaseDisplay();

        _continuePrompt = TextPool.get().write("Press " + Commands.get("Confirm") + " to enter the next tournament", Screen.pos(10, 10));

        Persistence.get().save();
    }

    private void updateDiseaseDisplay() {
        _topDiseases.update();
        _bottomDiseases.update();

        _populationHud.recalcIcons();
        NumberFormat f = NumberFormat.getNumberInstance();
        _populationCountDisplay.setMessage("Population of " + _regionName + "\n" + f.format(_population.getSize()) + " people");
    }

    private void simluatePopulationChange() {
        int totalDeaths = _bottomDiseases.totalDeaths(_population) + _topDiseases.totalDeaths(_population);
        _population.setSize(_population.getSize() - totalDeaths);
        _population.grow();
        updateDiseaseDisplay();
    }

    public void tournamentResult(boolean win) {
        if (win) {
            _topDiseases.disableOne();
            _tournamentWins++;
            MetaData.printWin();
        }
        else {
            MetaData.printLose();
        }
        _bottomDiseases.disableOne();
        simluatePopulationChange();
        _tournamentsPlayed++;
    }

    @Override
    public void draw() {
        _populationHud.draw();
    }

    private boolean gameFinished() {
        return _tournamentsPlayed >= GameConfig.NumberOfTournaments;
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || GameConfig.DevBotEnabled) {
            if (!gameFinished()) {
                StateManager.get().push(new LoadArena());
            }
            else {
                StateManager.get().push(new EndGame(_tournamentWins));
            }
        }
        if (GameConfig.DevPopulationTest) {
            boolean a = InputWrapper.pop();
            boolean b = InputWrapper.push();
            boolean c = InputWrapper.moveRight();
            if (a || b || c) {
                if (gameFinished() || c) {
                    StateManager.reset().push(new PopulationOverview());
                }
                else {
                    tournamentResult(a);
                }
            }
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        if (_tournamentsPlayed >= GameConfig.NumberOfTournaments - 1) {
            _continuePrompt.setMessage("Press " + Commands.get("Confirm") + " to see the outcome of your efforts.");
        }
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return "PopulationOverview";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
