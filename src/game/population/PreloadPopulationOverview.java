package game.population;

import game.core.BackgroundCache;
import game.core.GameConfig;
import game.core.PreloaderState;
import game.core.UIConfig;
import sps.core.Point2;
import sps.preload.PreloadChainLink;
import sps.states.StateManager;

import java.util.ArrayList;

public class PreloadPopulationOverview extends PreloaderState {
    private PopulationOverviewPayload _payload;

    @Override
    public void onCreate() {
        _payload = new PopulationOverviewPayload();
        BackgroundCache.clear();

        _preloadChain.add(new PreloadChainLink("Location your region of the planet.") {
            @Override
            public void process() {
                _payload.setRegionName(PopulationOverview.getRegionName());
                _payload.setPopulation(new Population(GameConfig.StartingPopulationSize));
            }
        });
        _preloadChain.add(new PreloadChainLink("Collecting information about your region.") {
            @Override
            public void process() {
                _payload.setPopulationHUD(new PopulationHUD(_payload.getPopulation(), UIConfig.PopulationHUDSize(), UIConfig.PopulationHUDPosition(), Map.NO_SEED, new ArrayList<Point2>()));
            }
        });
        _preloadChain.add(new PreloadChainLink("Determining the hardest causes of death to solve.") {
            @Override
            public void process() {
                _payload.setTopCauseOfDeathMonitor(new DeathCauseMonitor(true));
            }
        });
        _preloadChain.add(new PreloadChainLink("Determining the simplest causes of death to solve.") {
            @Override
            public void process() {
                _payload.setBottomCauseOfDeathMonitor(new DeathCauseMonitor(false));
            }
        });
        _preloadChain.add(new PreloadChainLink("Downloading terrain details and settlement locations.") {
            @Override
            public void process() {
                _payload.getPopulationHud().regenerateTextures();
            }
        });
        _preloadChain.add(new PreloadChainLink("Simulating environment for this region's activities.", 3) {
            @Override
            public void process() {
                BackgroundCache.cacheScreenSize();
            }
        });
    }

    @Override
    public void onFinish() {
        StateManager.get().push(new PopulationOverview(_payload));
    }
}
