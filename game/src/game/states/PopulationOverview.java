package game.states;

import game.GameConfig;
import game.InputWrapper;
import game.population.DiseaseMonitor;
import game.population.Population;
import game.population.PopulationHUD;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.text.NumberFormat;

public class PopulationOverview implements State {

    private Population _population;

    private DiseaseMonitor _topMonitor;
    private DiseaseMonitor _bottomMonitor;

    private PopulationHUD _populationHud;
    private Text _populationCountDisplay;

    private Text _continuePrompt;

    private int _tournamentsPlayed = 0;
    private int _tournamentWins = 0;

    @Override
    public void create() {
        _population = new Population();
        Point2 hudSize = Screen.pos(40, 70);
        Point2 hudPosition = Screen.pos(30, 15);
        _populationHud = new PopulationHUD(_population, hudSize, hudPosition);

        _topMonitor = new DiseaseMonitor(true);
        _bottomMonitor = new DiseaseMonitor(false);


        _populationCountDisplay = TextPool.get().write("", Screen.pos(30, 90));

        updateDiseaseDisplay();

        _continuePrompt = TextPool.get().write("Press " + Commands.get("Confirm") + " to enter the next tournament", Screen.pos(10, 10));
    }

    private void updateDiseaseDisplay() {
        _topMonitor.update();
        _bottomMonitor.update();

        _populationHud.recalcIcons();
        NumberFormat f = NumberFormat.getNumberInstance();
        _populationCountDisplay.setMessage("Population: " + f.format(_population.getSize()) + " people");
    }

    private void simluatePopulationChange() {
        int totalDeaths = _bottomMonitor.totalDeaths(_population) + _topMonitor.totalDeaths(_population);
        _population.setSize(_population.getSize() - totalDeaths);
        _population.grow();
        updateDiseaseDisplay();
    }

    public void tournamentResult(boolean win) {
        if (win) {
            _topMonitor.disableOne();
            _tournamentWins++;
        }
        _bottomMonitor.disableOne();
        simluatePopulationChange();
        _tournamentsPlayed++;
    }

    @Override
    public void draw() {
        _populationHud.draw();
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            if (_tournamentsPlayed < GameConfig.NumberOfTournaments) {
                StateManager.get().push(new LoadArena());
            }
            else {
                StateManager.get().push(new EndGame(_tournamentWins));
            }
        }
        if (GameConfig.DevPopulationTest) {
            if (InputWrapper.pop()) {
                tournamentResult(true);
            }
            if (InputWrapper.push()) {
                tournamentResult(false);
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
