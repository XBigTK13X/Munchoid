package game.population;

import game.EndGame;
import game.GameConfig;
import game.InputWrapper;
import game.Score;
import game.arena.PreloadArena;
import game.dev.DevConfig;
import game.dev.MetaData;
import game.save.GameSnapshot;
import game.save.Persistence;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import sps.bridge.Commands;
import sps.core.Loader;
import sps.core.RNG;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;
import sps.util.Markov;

import java.text.NumberFormat;

public class PopulationOverview implements State {
    private static final Markov __regionNames = Markov.get(Loader.get().data("region_name_seed.txt"), 2);

    private Population _population;

    private DeathCauseMonitor _topCauses;
    private DeathCauseMonitor _bottomCauses;

    private String _regionName;
    private PopulationHUD _populationHud;
    private Text _populationCountDisplay;

    private Text _continuePrompt;

    private int _tournamentsPlayed = 0;
    private int _tournamentWins = 0;

    private DeathCauseEradicated _eradicated;

    private boolean _restoredFromSaveFile;

    private PreloadPopulationOverview.Payload _preload;

    private Text _savingNotice;

    public PopulationOverview(PreloadPopulationOverview.Payload preload) {
        _preload = preload;
    }

    public PopulationOverview(GameSnapshot snapshot) {
        _restoredFromSaveFile = true;

        _regionName = snapshot.RegionName;
        _population = snapshot.Population;
        _populationHud = snapshot.PopulationHud;
        _populationHud.regenerateTextures();
        _topCauses = snapshot.TopDeathCauses;
        _bottomCauses = snapshot.BottomDeathCauses;
        _tournamentsPlayed = snapshot.TournamentsPlayed;
        _tournamentWins = snapshot.TournamentWins;
    }

    public GameSnapshot takeSnapshot() {
        GameSnapshot result = new GameSnapshot();
        result.Score = Score.get();
        result.Population = _population;
        result.BottomDeathCauses = _bottomCauses;
        result.TopDeathCauses = _topCauses;
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

            _topCauses = _preload.getTop();
            _bottomCauses = _preload.getBottom();

            _regionName = __regionNames.makeWord(RNG.next(7, 10));
            _regionName = WordUtils.capitalize(_regionName);
        }

        _topCauses.generateDisplay();
        _bottomCauses.generateDisplay();
        _populationCountDisplay = TextPool.get().write("", Screen.pos(30, 95));

        updateDeathDisplays();
        _continuePrompt = TextPool.get().write("Press " + Commands.get("Confirm") + " to enter the next tournament", Screen.pos(10, 10));

        _savingNotice = TextPool.get().write("", Screen.pos(20, 50));

        StateManager.get().showTutorial();
    }

    private int saveDotsMax = 5;
    private int saveDots = 0;
    private CoolDown _saveUpdate = new CoolDown(.5f);

    private void updateSaveMessage() {
        if (_saveUpdate.updateAndCheck()) {
            saveDots = (saveDots + 1) % saveDotsMax;
            _savingNotice.setMessage("Saving" + StringUtils.repeat(".", saveDots));
        }
    }

    private void updateDeathDisplays() {
        _topCauses.update();
        _bottomCauses.update();

        _populationHud.recalcIcons();
        NumberFormat f = NumberFormat.getNumberInstance();
        _populationCountDisplay.setMessage("Population of " + _regionName + "\n" + f.format(_population.getSize()) + " people");

        Persistence.get().autoSave();
    }

    private void simluatePopulationChange() {
        int totalDeaths = _bottomCauses.totalDeaths(_population) + _topCauses.totalDeaths(_population);
        _population.setSize(_population.getSize() - totalDeaths);
        _population.grow();
        updateDeathDisplays();
    }

    public void tournamentResult(boolean win) {
        DeathCause top = null;
        if (win) {
            top = _topCauses.disableOne();
            _tournamentWins++;
            MetaData.printWin();
        }
        else {
            MetaData.printLose();
        }
        _eradicated = new DeathCauseEradicated(top, _bottomCauses.disableOne());
        _tournamentsPlayed++;
    }

    @Override
    public void draw() {
        if (Persistence.get().isBusy()) {
            updateSaveMessage();
            _savingNotice.setVisible(true);
            return;
        }
        _savingNotice.setVisible(false);

        _populationHud.draw();
        if (_eradicated != null) {
            _eradicated.draw();
        }
    }

    private boolean gameFinished() {
        return _tournamentsPlayed >= GameConfig.NumberOfTournaments;
    }

    private void nextState() {
        if (!gameFinished()) {
            StateManager.get().push(new PreloadArena());
        }
        else {
            StateManager.get().push(new EndGame(_tournamentWins));
        }
    }

    @Override
    public void update() {
        if (Persistence.get().isBusy()) {
            return;
        }

        if (_eradicated != null) {
            if (DevConfig.BotEnabled) {
                simluatePopulationChange();
                nextState();
                _eradicated = null;
            }
            else {
                if (_eradicated.isActive()) {
                    _eradicated.update();
                }
                if (!_eradicated.isActive()) {
                    simluatePopulationChange();
                    _eradicated = null;
                }
            }
        }
        else {
            if (gameFinished()) {
                _continuePrompt.setMessage("Press " + Commands.get("Confirm") + " to see the outcome of your efforts.");
            }
            if (InputWrapper.confirm() || DevConfig.BotEnabled) {
                nextState();
            }
            if (DevConfig.PopulationTest) {
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
    public void pause() {
    }
}
