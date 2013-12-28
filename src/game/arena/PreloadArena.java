package game.arena;

import game.battle.TimerGraphic;
import game.core.GameConfig;
import game.dev.DevConfig;
import sps.color.Color;
import sps.core.Point2;
import sps.preload.PreloadChain;
import sps.preload.PreloadChainLink;
import sps.states.State;
import sps.states.StateManager;

public class PreloadArena implements State {
    private ArenaPayload _payload;

    @Override
    public void create() {
        _payload = new ArenaPayload();
        _preloadChain = new PreloadChain() {
            @Override
            public void finish() {
                StateManager.get().push(new Arena(_payload));
            }
        };
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
        int creatureCount = DevConfig.TournyTest ? 0 : GameConfig.CreatureLimit;
        _preloadChain.add(new PreloadChainLink("Collecting mental fragments from combatents.", creatureCount) {
            @Override
            public void process() {
                _payload.cache(new Catchable(_payload.getPlayer(), _payload.getFloor()));
            }
        });
    }

    private PreloadChain _preloadChain;

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
        return "PreloadArena";
    }

    @Override
    public void pause() {
    }
}
