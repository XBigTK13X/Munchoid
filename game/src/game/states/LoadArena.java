package game.states;

import game.GameConfig;
import game.arena.Catchable;
import game.arena.Floor;
import game.arena.Player;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Screen;

public class LoadArena implements State {
    private Arena.Preload _preload;
    private int _preloadedItems;
    private int _preloadedItemsTarget;
    private Text _loadingMessage;
    private LoadingMeter _meter;

    @Override
    public void create() {
        //Floor + Player + Creatures
        _preloadedItemsTarget = 1 + 1 + GameConfig.CreatureLimit;
        _loadingMessage = TextPool.get().write(getMessage(), Screen.pos(10, 60));
        _preload = new Arena.Preload();
        _meter = new LoadingMeter();
    }

    private String getMessage() {
        return "Preparing the game: " + _preloadedItems + "/" + _preloadedItemsTarget + " objects loaded";
    }

    @Override
    public void draw() {
        _meter.draw();
    }

    @Override
    public void update() {
        switch (_preloadedItems) {
            case 0:
                _preload.cache(new Floor());
                break;
            case 1:
                _preload.cache(new Player(_preload.getFloor()));
                break;
            default:
                _preload.cache(new Catchable(_preload.getPlayer()));
                break;
        }

        _preloadedItems++;
        if (_preloadedItems >= _preloadedItemsTarget) {
            StateManager.get().push(new Arena(_preload));
        }
        else {
            _meter.scaleWidth((int) ((_preloadedItems / (float) _preloadedItemsTarget) * 100));
            _loadingMessage.setMessage(getMessage());
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
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
