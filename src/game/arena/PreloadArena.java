package game.arena;

import game.DevConfig;
import game.GameConfig;
import game.battle.TimerGraphic;
import game.ui.Meter;
import sps.color.Color;
import sps.color.Colors;
import sps.core.Point2;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PreloadArena implements State {
    public static class Payload {
        private List<Catchable> _catchables = new ArrayList<Catchable>();
        private Floor _floor;
        private Player _player;
        private TimerGraphic _timer;

        public void cache(Player player) {
            _player = player;
        }

        public Player getPlayer() {
            return _player;
        }

        public void cache(Catchable catchable) {
            _catchables.add(catchable);
        }

        public List<Catchable> getCatchables() {
            return _catchables;
        }

        public void cache(Floor floor) {
            _floor = floor;
        }

        public Floor getFloor() {
            return _floor;
        }

        public void cache(TimerGraphic timer) {
            _timer = timer;
        }

        public TimerGraphic getTimer() {
            return _timer;
        }
    }

    private Payload _arenaPieces;
    private int _preloadedItems;
    private int _preloadedItemsTarget;
    private Text _loadingMessage;
    private Meter _loadingMeter;

    @Override
    public void create() {
        //Floor + Player + Timer + Creatures
        int creatureCount = DevConfig.TournyTest ? 0 : GameConfig.CreatureLimit;
        _preloadedItemsTarget = 1 + 1 + 1 + creatureCount;
        _loadingMessage = TextPool.get().write(getMessage(), Screen.pos(10, 60));
        _arenaPieces = new Payload();
        _loadingMeter = new Meter(90, 5, Colors.randomPleasant(), Screen.pos(5, 30), false);
        _preloadedItems = -1;
    }

    private String getMessage() {
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
                //Gives the preloader a chance to show up before the Floor is generated
                break;
            case 0:
                _arenaPieces.cache(new Floor());
                break;
            case 1:
                _arenaPieces.cache(new Player(_arenaPieces.getFloor()));
                break;
            case 2:
                _arenaPieces.cache(new TimerGraphic(true, new Point2(0, 0), Color.WHITE.newAlpha(.75f)));
            default:
                _arenaPieces.cache(new Catchable(_arenaPieces.getPlayer(), _arenaPieces.getFloor()));
                break;
        }

        _preloadedItems++;
        if (_preloadedItems >= _preloadedItemsTarget) {
            StateManager.get().push(new Arena(_arenaPieces));
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
        return "PreloadArena";
    }

    @Override
    public void pause() {
    }
}
