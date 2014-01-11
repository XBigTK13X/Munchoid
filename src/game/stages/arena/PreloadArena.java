package game.stages.arena;

import game.stages.battle.TimerGraphic;
import game.config.GameConfig;
import sps.preload.PreloaderState;
import game.app.dev.DevConfig;
import sps.color.Color;
import sps.core.Point2;
import sps.preload.PreloadChainLink;
import sps.states.StateManager;

public class PreloadArena extends PreloaderState {
    private ArenaPayload _payload;

    @Override
    public void onCreate() {
        _payload = new ArenaPayload();
        _preloadChain.add(new PreloadChainLink("Locating the nearest Munchoid arena.") {
            @Override
            public void process() {
                _payload.cache(new Floor());
            }
        });
        _preloadChain.add(new PreloadChainLink("Uploading avatar to the arena.") {
            @Override
            public void process() {
                _payload.cache(new Player(_payload.getFloor()));
            }
        });
        _preloadChain.add(new PreloadChainLink("Selecting a time keeper to watch the events.") {
            @Override
            public void process() {
                _payload.cache(new TimerGraphic(true, new Point2(0, 0), Color.WHITE.newAlpha(.75f)));
            }
        });
        int creatureCount = DevConfig.TournyTest ? 1 : GameConfig.CreatureLimit;
        _preloadChain.add(new PreloadChainLink("Collecting mental fragments from combatents.", creatureCount) {
            @Override
            public void process() {
                _payload.cache(new Catchable(_payload.getPlayer(), _payload.getFloor()));
            }
        });
    }

    @Override
    public void onFinish() {
        StateManager.get().push(new Arena(_payload));
    }
}
