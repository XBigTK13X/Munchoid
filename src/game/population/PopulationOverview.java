package game.population;

import com.badlogic.gdx.Gdx;
import game.arena.PreloadArena;
import game.core.EndGame;
import game.core.GameConfig;
import game.core.InputWrapper;
import game.core.Score;
import game.dev.DevConfig;
import game.dev.MetaData;
import game.save.GameSnapshot;
import game.save.Persistence;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import sps.bridge.Commands;
import sps.color.Color;
import sps.core.Loader;
import sps.core.RNG;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Meter;
import sps.ui.MultiText;
import sps.util.CoolDown;
import sps.util.Markov;

import java.text.NumberFormat;

public class PopulationOverview implements State {
    private static final Markov __regionNames = Markov.get(Loader.get().data("region_name_seed.txt"), 2);

    public static String getRegionName() {
        return WordUtils.capitalize(__regionNames.makeWord(RNG.next(7, 10)));
    }

    private Population _population;
    private int _peopleToKill = 0;
    private int _peopleBorn = 0;
    private int _killSpeed;
    private int _birthSpeed;

    private DeathCauseMonitor _topCauses;
    private DeathCauseMonitor _bottomCauses;

    private Meter _solutionsMeter;

    private String _regionName;
    private PopulationHUD _populationHud;
    private Text _populationCountDisplay;

    private MultiText _playByPlay;

    private Text _continuePrompt;

    private int _tournamentsPlayed = 0;
    private int _tournamentWins = 0;

    private DeathCauseEradicated _eradicated;

    private PopulationOverviewPayload _payload;

    private Text _savingNotice;

    public PopulationOverview(PopulationOverviewPayload preload) {
        _payload = preload;
    }

    public GameSnapshot takeSnapshot() {
        GameSnapshot result = new GameSnapshot();
        result.Victories = Score.get().victories();
        result.AcceptedMerges = Score.get().acceptedMerges();
        result.RejectedMerges = Score.get().rejectedMerges();
        result.HealthRemaining = Score.get().healthRemaining();
        result.Chomps = Score.get().chomps();
        result.PetVariety = Score.get().petVariety();
        result.PetPower = Score.get().petPower();
        result.PopulationSize = _population.getSize();
        result.SaveFormatVersion = GameSnapshot.CurrentSaveFormatVersion;
        result.TournamentsPlayed = _tournamentsPlayed;
        result.TournamentWins = _tournamentWins;
        result.RegionName = _regionName;
        result.RegionMapSeed = _populationHud.getMapSeed();
        result.SettlementLocations = _populationHud.getSettlementLocations();
        result.BottomDeathCauses = _bottomCauses.getPersistable();
        result.TopDeathCauses = _topCauses.getPersistable();
        return result;
    }

    @Override
    public void create() {
        _population = _payload.getPopulation();
        _populationHud = _payload.getPopulationHud();

        _topCauses = _payload.getTop();
        _bottomCauses = _payload.getBottom();

        _regionName = _payload.getRegionName();

        _topCauses.generateDisplay();
        _bottomCauses.generateDisplay();
        _populationCountDisplay = TextPool.get().write("", GameConfig.PopulationCountPosition());

        TextPool.get().write("Causes of Death Solved", GameConfig.PopulationSolutionsCaptionPosition());
        _solutionsMeter = new Meter(40, 5, Color.GREEN, GameConfig.PopulationSolutionMeterPosition(), false);

        _playByPlay = new MultiText(GameConfig.PopulationPlayByPlayPosition(), 25, Color.BLUE.newAlpha(.75f), (int) GameConfig.PopulationPlayByPlaySize().X, (int) GameConfig.PopulationPlayByPlaySize().Y);
        _playByPlay.add("Welcome to the overview for " + _regionName);

        _tournamentsPlayed = _payload.getTournamentsPlayed();
        _tournamentWins = _payload.getTournamentWins();

        _populationHud.regenerateTextures();
        updateDeathDisplays();
        _continuePrompt = TextPool.get().write("Press " + Commands.get("Confirm") + " to enter the next tournament", GameConfig.PopulationContinuePosition());

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

    private void updatePopulationCount() {
        NumberFormat f = NumberFormat.getNumberInstance();
        _populationCountDisplay.setMessage("Population of " + _regionName + "\n" + f.format(_population.getSize()) + " people");
        _populationHud.recalcIcons();
    }

    private void updateDeathDisplays() {
        _topCauses.update();
        _bottomCauses.update();

        updatePopulationCount();

        int activeDiseases = _topCauses.getActiveCount() + _bottomCauses.getActiveCount();
        int totalDiseases = GameConfig.NumberOfTournaments * 2;
        _solutionsMeter.setPercent((int) (100 * ((float) (totalDiseases - activeDiseases) / totalDiseases)));

        Persistence.get().autoSave();
    }

    private void simluatePopulationChange() {
        _peopleToKill = _bottomCauses.totalDeaths(_population) + _topCauses.totalDeaths(_population);
        _peopleBorn = _population.getGrowth();
        _killSpeed = (int) (_peopleToKill * Gdx.graphics.getDeltaTime());
        _birthSpeed = (int) (_peopleBorn * Gdx.graphics.getDeltaTime());
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

    private void displayEradicatedNotice() {
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

    private void handleUserInput() {
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

    @Override
    public void update() {
        if (Persistence.get().isBusy()) {
            return;
        }

        if (_eradicated != null) {
            displayEradicatedNotice();
            return;
        }

        if (_peopleToKill > 0) {
            if (_killSpeed > _peopleToKill) {
                _killSpeed = _peopleToKill;
            }
            _peopleToKill -= _killSpeed;
            _population.setSize(_population.getSize() - _killSpeed);
            updatePopulationCount();
        }
        else if (_peopleBorn > 0) {
            if (_birthSpeed > _peopleBorn) {
                _birthSpeed = _peopleBorn;
            }
            _peopleBorn -= _birthSpeed;
            _population.setSize(_population.getSize() + _birthSpeed);
            updatePopulationCount();
        }
        else {
            handleUserInput();
        }
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
        _solutionsMeter.draw();
        _playByPlay.draw();
        if (_eradicated != null) {
            _eradicated.draw();
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
