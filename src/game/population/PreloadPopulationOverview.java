package game.population;

import game.BackgroundCache;
import game.ui.Meter;
import sps.color.Colors;
import sps.core.Point2;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.text.NumberFormat;

public class PreloadPopulationOverview implements State {
    public class Payload {
        private Population _population;
        private PopulationHUD _populationHud;
        private DeathCauseMonitor _top;
        private DeathCauseMonitor _bottom;

        public void cache(Population population) {
            _population = population;
        }


        public Population getPopulation() {
            return _population;
        }

        public void cache(PopulationHUD populationHud) {
            _populationHud = populationHud;
        }

        public PopulationHUD getPopulationHud() {
            return _populationHud;
        }

        public void cache(DeathCauseMonitor monitor, boolean top) {
            if (top) {
                _top = monitor;
            }
            else {
                _bottom = monitor;
            }
        }

        public DeathCauseMonitor getTop() {
            return _top;
        }

        public DeathCauseMonitor getBottom() {
            return _bottom;
        }
    }

    private Payload _payload;
    private int _preloadedItems;
    private int _preloadedItemsTarget;
    private Text _loadingMessage;
    private Meter _loadingMeter;

    @Override
    public void create() {
        //PopHud + Pop + Top + Bottom + 3 backgrounds
        _preloadedItemsTarget = 1 + 1 + 1 + 1 + 3;
        _loadingMessage = TextPool.get().write(getMessage(), Screen.pos(10, 60));
        _payload = new Payload();
        _loadingMeter = new Meter(90, 5, Colors.randomPleasant(), Screen.pos(5, 30), false);
        _preloadedItems = -1;
        BackgroundCache.clear();
    }

    private String getMessage() {
        switch (_preloadedItems) {
            case 0:
                return "Finding a population to serve.";
            case 1:
                return "Collecting information about your region.";
            case 2:
                return "Determining the hardest causes of death to solve.";
            case 3:
                return "Determining the easiest causes of death to solve";
            case 4:
                return "Downloading terrain details and settlement locations.";
        }
        return "Simulating environment for this region's activities.";
    }

    private static final NumberFormat _df = NumberFormat.getPercentInstance();

    private String getProgress() {
        return _df.format(((float) _preloadedItems / _preloadedItemsTarget)) + " complete";
    }

    @Override
    public void draw() {
        _loadingMeter.draw();
    }

    @Override
    public void update() {
        switch (_preloadedItems) {
            case -1:
                //Gives the preloader a chance to show up before any generation occurs
                break;
            case 0:
                _payload.cache(new Population());
                break;
            case 1:
                Point2 hudSize = Screen.pos(40, 70);
                Point2 hudPosition = Screen.pos(30, 15);
                _payload.cache(new PopulationHUD(_payload.getPopulation(), hudSize, hudPosition));
                break;
            case 2:
                _payload.cache(new DeathCauseMonitor(false), false);
                break;
            case 3:
                _payload.cache(new DeathCauseMonitor(true), true);
            case 4:
                _payload.getPopulationHud().regenerateTextures();
            default:
                BackgroundCache.cacheScreenSize();
                break;
        }

        _preloadedItems++;
        if (_preloadedItems >= _preloadedItemsTarget) {
            StateManager.get().push(new PopulationOverview(_payload));
        }
        else {
            _loadingMeter.scale((int) ((_preloadedItems / (float) _preloadedItemsTarget) * 100));
            _loadingMessage.setMessage(getMessage() + "\n" + getProgress());
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
        return "PreloadPopulationOverview";
    }

    @Override
    public void pause() {
    }
}
