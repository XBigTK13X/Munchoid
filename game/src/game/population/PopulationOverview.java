package game.population;

import game.*;
import game.arena.PreloadArena;
import game.save.GameSnapshot;
import game.save.Persistence;
import org.apache.commons.lang3.text.WordUtils;
import sps.bridge.Commands;
import sps.core.Loader;
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

    private boolean _restoredFromSaveFile;

    private PreloadPopulationOverview.Payload _preload;

    public PopulationOverview(PreloadPopulationOverview.Payload preload) {
        _preload = preload;
    }

    public PopulationOverview(GameSnapshot snapshot) {
        _restoredFromSaveFile = true;

        _regionName = snapshot.RegionName;
        _population = snapshot.Population;
        _populationHud = snapshot.PopulationHud;
        _populationHud.regenerateTextures();
        _topDiseases = snapshot.TopDiseases;
        _bottomDiseases = snapshot.BottomDiseases;
        _tournamentsPlayed = snapshot.TournamentsPlayed;
        _tournamentWins = snapshot.TournamentWins;
    }

    public GameSnapshot takeSnapshot() {
        GameSnapshot result = new GameSnapshot();
        result.Score = Score.get();
        result.Population = _population;
        result.BottomDiseases = _bottomDiseases;
        result.TopDiseases = _topDiseases;
        result.RegionName = _regionName;
        result.PopulationHud = _populationHud;
        result.Population = _population;
        result.RecordedVersion = GameSnapshot.Version;
        result.TournamentsPlayed = _tournamentsPlayed;
        result.TournamentWins = _tournamentWins;
        return result;
    }

    @Override
    public void create() {
        if (!_restoredFromSaveFile) {
            _population = _preload.getPopulation();
            _populationHud = _preload.getPopulationHud();

            _topDiseases = _preload.getTop();
            _bottomDiseases = _preload.getBottom();

            _regionName = __regionNames.makeWord(RNG.next(7, 10));
            _regionName = WordUtils.capitalize(_regionName);
        }

        _topDiseases.generateDisplay();
        _bottomDiseases.generateDisplay();
        _populationCountDisplay = TextPool.get().write("", Screen.pos(30, 95));

        updateDiseaseDisplay();
        _continuePrompt = TextPool.get().write("Press " + Commands.get("Confirm") + " to enter the next tournament", Screen.pos(10, 10));
    }

    private void updateDiseaseDisplay() {
        _topDiseases.update();
        _bottomDiseases.update();

        _populationHud.recalcIcons();
        NumberFormat f = NumberFormat.getNumberInstance();
        _populationCountDisplay.setMessage("Population of " + _regionName + "\n" + f.format(_population.getSize()) + " people");

        Persistence.get().autoSave();
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
        _tournamentsPlayed++;
        simluatePopulationChange();
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
        if (gameFinished()) {
            _continuePrompt.setMessage("Press " + Commands.get("Confirm") + " to see the outcome of your efforts.");
        }
        if (InputWrapper.confirm() || GameConfig.DevBotEnabled) {
            if (!gameFinished()) {
                StateManager.get().push(new PreloadArena());
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
                    StateManager.reset().push(new PreloadPopulationOverview());
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
