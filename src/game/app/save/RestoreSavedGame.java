package game.app.save;

import game.app.core.BackgroundCache;
import sps.preload.PreloaderState;
import config.UIConfig;
import game.app.core.WorldScore;
import game.stages.population.*;
import game.stages.pregame.MainMenu;
import sps.preload.PreloadChainLink;
import sps.states.StateManager;

public class RestoreSavedGame extends PreloaderState {
    private GameSnapshot _snapshot;
    private PopulationOverviewPayload _payload;

    @Override
    public void onFinish() {
        StateManager.get().push(new PopulationOverview(_payload));
    }

    @Override
    public void onCreate() {
        _payload = new PopulationOverviewPayload();
        _preloadChain.add(new PreloadChainLink("Reading auto save file.") {
            @Override
            public void process() {
                try {
                    _snapshot = Persistence.get().autoLoad();
                    if (_snapshot == null) {
                        StateManager.get().rollBackTo(MainMenu.class);
                    }
                }
                catch (Exception e) {
                    StateManager.get().rollBackTo(MainMenu.class);
                }
            }
        });
        _preloadChain.add(new PreloadChainLink("Restoring score data.") {
            @Override
            public void process() {
                WorldScore.reset(_snapshot.TournamentWins, _snapshot.TournamentLosses, _snapshot.CumulativeArenaScore);
            }
        });
        _preloadChain.add(new PreloadChainLink("Resolving region name.") {
            @Override
            public void process() {
                _payload.setRegionName(_snapshot.RegionName);
            }
        });
        _preloadChain.add(new PreloadChainLink("Restoring population.") {
            @Override
            public void process() {
                _payload.setPopulation(new Population(_snapshot.PopulationSize));
            }
        });
        _preloadChain.add(new PreloadChainLink("Rebuilding population HUD.") {
            @Override
            public void process() {
                _payload.setPopulationHUD(new PopulationHUD(_payload.getPopulation(), UIConfig.PopulationHUDSize(), UIConfig.PopulationHUDPosition(), _snapshot.RegionMapSeed, _snapshot.SettlementLocations));
            }
        });
        _preloadChain.add(new PreloadChainLink("Finding registered causes of death that are hardest to solve.") {
            @Override
            public void process() {
                DeathCauseMonitor top = new DeathCauseMonitor(true);
                top.fromPersistable(_snapshot.TopDeathCauses);
                _payload.setTopCauseOfDeathMonitor(top);
            }
        });
        _preloadChain.add(new PreloadChainLink("Finding registered causes of death that are simplest to solve.") {
            @Override
            public void process() {
                DeathCauseMonitor bottom = new DeathCauseMonitor(false);
                bottom.fromPersistable(_snapshot.BottomDeathCauses);
                _payload.setBottomCauseOfDeathMonitor(bottom);
            }
        });
        _preloadChain.add(new PreloadChainLink("Clearing old environment textures.") {
            @Override
            public void process() {
                BackgroundCache.clear();
            }
        });
        _preloadChain.add(new PreloadChainLink("Generating new environment textures.", 3) {
            @Override
            public void process() {
                BackgroundCache.cacheScreenSize();
            }
        });
    }
}
