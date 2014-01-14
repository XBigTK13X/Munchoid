package game.stages.population;

import com.badlogic.gdx.Gdx;
import game.app.config.GameConfig;
import game.app.config.UIConfig;
import game.stages.arena.PreloadArena;
import game.app.core.*;
import game.app.dev.DevConfig;
import game.app.dev.MetaData;
import game.app.save.GameSnapshot;
import game.app.save.Persistence;
import game.app.tutorial.Tutorials;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.color.Colors;
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
    private int _settlementGrowth = 0;
    private int _settlementDecay = 0;
    private int _peopleThatHaveDied = 0;
    private int _peopleThatWereBorn = 0;
    private int _peopleToKill = 0;
    private int _peopleToBirth = 0;
    private int _killSpeed;
    private int _birthSpeed;

    private DeathCauseMonitor _topCauses;
    private DeathCauseMonitor _bottomCauses;
    private int _activeCauses;

    private Meter _solutionsMeter;

    private String _regionName;
    private PopulationHUD _populationHud;
    private Text _populationCountDisplay;

    private MultiText _playByPlay;

    private Text _continuePrompt;

    private DeathCauseEradicated _eradicated;

    private PopulationOverviewPayload _payload;

    private Text _savingNotice;

    public PopulationOverview(PopulationOverviewPayload preload) {
        _payload = preload;
    }

    public GameSnapshot takeSnapshot() {
        GameSnapshot result = new GameSnapshot();
        result.CumulativeArenaScore = WorldScore.ArenaTotal;
        result.TournamentWins = WorldScore.TournamentWins;
        result.TournamentLosses = WorldScore.TournamentLosses;

        result.PopulationSize = _population.getSize();
        result.SaveFormatVersion = GameSnapshot.CurrentSaveFormatVersion;
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
        _populationCountDisplay = TextPool.get().write("", UIConfig.PopulationCountPosition());

        TextPool.get().write("Salvation Progress", UIConfig.PopulationSolutionsCaptionPosition());
        _solutionsMeter = new Meter(40, 5, Color.GREEN, UIConfig.PopulationSolutionMeterPosition(), false);

        _playByPlay = new MultiText(UIConfig.PopulationPlayByPlayPosition(), 30, Colors.randomPleasant().newAlpha(.50f), (int) UIConfig.PopulationPlayByPlaySize().X, (int) UIConfig.PopulationPlayByPlaySize().Y);
        _playByPlay.add("Welcome to the overview for " + _regionName);
        _playByPlay.setBackgroundDepth(DrawDepths.get("PlayByPlayBackground"));
        _playByPlay.setTextDepth(DrawDepths.get("PlayByPlayText"));

        _populationHud.regenerateTextures();
        updateDeathDisplays();
        _continuePrompt = TextPool.get().write("Press " + Commands.get("Confirm") + " to enter the next tournament", UIConfig.PopulationContinuePosition());

        _savingNotice = TextPool.get().write("", Screen.pos(20, 50));

        Tutorials.get().show();
        Persistence.get().autoSave();
    }

    private int saveDotsMax = 5;
    private int saveDots = 0;
    private CoolDown _saveUpdate = new CoolDown(.5f);

    private NumberFormat _nF = NumberFormat.getNumberInstance();

    private void updateSaveMessage() {
        if (_saveUpdate.updateAndCheck()) {
            saveDots = (saveDots + 1) % saveDotsMax;
            _savingNotice.setMessage("Saving" + StringUtils.repeat(".", saveDots));
        }
    }


    private void updatePopulationCount() {
        _populationCountDisplay.setMessage("Population of " + _regionName + "\n\t" + _nF.format(_population.getSize()) + " people");
        int growth = _populationHud.recalcIcons();
        if (growth < 0) {
            _settlementDecay += growth;
        }
        if (growth > 0) {
            _settlementGrowth += growth;
        }
    }

    private void updateDeathDisplays() {
        _topCauses.update();
        _bottomCauses.update();

        updatePopulationCount();

        _activeCauses = _topCauses.getActiveCount() + _bottomCauses.getActiveCount();
        int totalDiseases = GameConfig.NumberOfTournaments * 2;
        _solutionsMeter.setPercent((int) (100 * ((float) (totalDiseases - _activeCauses) / totalDiseases)));
    }

    private void simluatePopulationChange() {
        _peopleToKill = _bottomCauses.totalDeaths(_population) + _topCauses.totalDeaths(_population);
        _peopleThatHaveDied = _peopleToKill;
        _peopleToBirth = _population.getGrowth();
        _peopleThatWereBorn = _peopleToBirth;
        _killSpeed = (int) (_peopleToKill * Gdx.graphics.getDeltaTime());
        _birthSpeed = (int) (_peopleToBirth * Gdx.graphics.getDeltaTime());
        updateDeathDisplays();
    }

    public void tournamentResult(boolean win) {
        DeathCause top = null;
        WorldScore.ArenaTotal += ArenaScore.get().total();
        if (win) {
            top = _topCauses.disableOne();
            WorldScore.TournamentWins++;
            MetaData.printWin();
        }
        else {
            WorldScore.TournamentLosses++;
            MetaData.printLose();
        }
        _eradicated = new DeathCauseEradicated(top, _bottomCauses.disableOne());
        _playByPlay.setVisible(false);
        _continuePrompt.setVisible(false);
        _settlementGrowth = 0;
    }

    private boolean gameFinished() {
        return (WorldScore.TournamentWins + WorldScore.TournamentLosses) >= GameConfig.NumberOfTournaments;
    }

    private void nextState() {
        if (!gameFinished()) {
            StateManager.get().push(new PreloadArena());
        }
        else {
            StateManager.get().push(new EndGame());
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
                _playByPlay.setVisible(true);
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
                    StateManager.clearTimes();
                    Tutorials.get().clearCompletion();
                    ArenaScore.reset();
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
            if (_peopleToKill <= 0) {
                _playByPlay.add(_nF.format(_peopleThatHaveDied) + " people have died from " + _activeCauses + " solveable causes.");
            }
            updatePopulationCount();
        }
        else if (_peopleToBirth > 0) {
            if (_birthSpeed > _peopleToBirth) {
                _birthSpeed = _peopleToBirth;
            }
            _peopleToBirth -= _birthSpeed;
            _population.setSize(_population.getSize() + _birthSpeed);
            if (_peopleToBirth <= 0) {
                _playByPlay.add(_nF.format(_peopleThatWereBorn) + " people were born in " + _regionName + ".");

                if (_settlementGrowth > 0) {
                    boolean p = _settlementGrowth == 1;
                    _playByPlay.add((p ? "A" : _settlementGrowth) + " new " + (p ? "settlement was" : "settlements were") + " founded.");
                }
                if (_settlementDecay < 0) {
                    boolean p = _settlementDecay == -1;
                    _playByPlay.add((p ? "A" : -1 * _settlementDecay) + " " + (p ? "settlement" : "settlements") + " fell apart.");
                }
                if (_settlementDecay == 0 && _settlementGrowth == 0) {
                    _playByPlay.add("No settlements were founded or lost.");
                }
                _playByPlay.add("");
                Persistence.get().autoSave();
            }
            updatePopulationCount();
        }
        else {
            _continuePrompt.setVisible(true);
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

        if (_eradicated != null) {
            _eradicated.draw();
        }
        else {
            _populationHud.draw();
            _solutionsMeter.draw();
            _playByPlay.draw();
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
