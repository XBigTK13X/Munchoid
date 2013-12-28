package game.population;

import game.core.BackgroundCache;
import sps.core.Point2;
import sps.display.Screen;
import sps.preload.PreloadChain;
import sps.preload.PreloadChainLink;
import sps.states.State;
import sps.states.StateManager;

public class PreloadPopulationOverview implements State {
    private PopulationOverviewPayload _payload;
    private PreloadChain _preloadChain;

    @Override
    public void create() {
        _payload = new PopulationOverviewPayload();
        BackgroundCache.clear();

        _preloadChain = new PreloadChain() {
            @Override
            public void finish() {
                StateManager.get().push(new PopulationOverview(_payload));
            }
        };

        _preloadChain.add(new PreloadChainLink("Location your region of the planet.") {
            @Override
            public void process() {
                _payload.cache(new Population());
            }
        });
        _preloadChain.add(new PreloadChainLink("Collecting information about your region.") {
            @Override
            public void process() {
                Point2 hudSize = Screen.pos(40, 70);
                Point2 hudPosition = Screen.pos(30, 15);
                _payload.cache(new PopulationHUD(_payload.getPopulation(), hudSize, hudPosition));
            }
        });
        _preloadChain.add(new PreloadChainLink("Determining the hardest causes of death to solve.") {
            @Override
            public void process() {
                _payload.cache(new DeathCauseMonitor(false), false);
            }
        });
        _preloadChain.add(new PreloadChainLink("Determining the simplest causes of death to solve.") {
            @Override
            public void process() {
                _payload.cache(new DeathCauseMonitor(true), true);
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
    public void draw() {
        _preloadChain.draw();
    }

    @Override
    public void update() {
        _preloadChain.update();
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
        return "PreloadPopulationOverview";
    }

    @Override
    public void pause() {
    }
}
