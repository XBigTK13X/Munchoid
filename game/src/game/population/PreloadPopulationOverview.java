package game.population;

import game.ui.Meter;
import sps.core.Point2;
import sps.display.Screen;
import sps.draw.Colors;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.text.NumberFormat;

public class PreloadPopulationOverview implements State {
    public class Payload {
        private Population _population;
        private PopulationHUD _populationHud;
        private DiseaseMonitor _top;
        private DiseaseMonitor _bottom;

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

        public void cache(DiseaseMonitor monitor, boolean top) {
            if (top) {
                _top = monitor;
            }
            else {
                _bottom = monitor;
            }
        }

        public DiseaseMonitor getTop() {
            return _top;
        }

        public DiseaseMonitor getBottom() {
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
        //PopHud + Pop + Top + Bottom
        _preloadedItemsTarget = 1 + 1 + 1 + 1;
        _loadingMessage = TextPool.get().write(getMessage(), Screen.pos(10, 60));
        _payload = new Payload();
        _loadingMeter = new Meter(90, 5, Colors.randomPleasant(), Screen.pos(5, 30), false);
        _preloadedItems = -1;
    }

    private String getMessage() {
        switch (_preloadedItems) {
            case 0:
                return "Generating a new population";
            case 1:
                return "Creating the HUD for your region";
            case 2:
                return "Selecting strongest causes of death";
            case 3:
                return "Selecting weakest causes of death";
            case 4:
                return "Generating textures";
        }
        return "Collecting mental fragments from all combatants. ";
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
                _payload.cache(new DiseaseMonitor(false), false);
                break;
            case 3:
                _payload.cache(new DiseaseMonitor(true), true);
            case 4:
                _payload.getPopulationHud().regenerateTextures();
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
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
