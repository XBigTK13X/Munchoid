package game.states;

import com.badlogic.gdx.Gdx;
import game.GameConfig;
import game.arena.Catchable;
import game.arena.Floor;
import game.arena.Player;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Screen;

import java.util.List;

public class Arena implements State {

    private static final Point2 __timerPos = Screen.pos(5, 95);
    private int _lastTime;
    private float _countDownSeconds;
    private Text _timerText;

    private static final Point2 __creatureTextPos = Screen.pos(55, 95);
    private Text _creatureText;
    private int _lastCreatureCount;

    private String timeDisplay() {
        return _lastTime == 1 ?
                "Time Remaining: " + _lastTime + " second" : "Time Remaining: " + _lastTime + " seconds";
    }

    private String creatureDisplay(int creatureCount) {
        return "Creatures remaining: " + creatureCount;
    }

    @Override
    public void create() {
        _countDownSeconds = GameConfig.ArenaTimeoutSeconds;
        _lastTime = (int) GameConfig.ArenaTimeoutSeconds;
        _timerText = TextPool.get().write(timeDisplay(), __timerPos);
        EntityManager.get().addEntity(new Floor());
        EntityManager.get().addEntity(new Player());
        for (int ii = 0; ii < GameConfig.CreatureLimit; ii++) {
            EntityManager.get().addEntity(new Catchable());
        }
        _creatureText = TextPool.get().write(creatureDisplay(GameConfig.CreatureLimit), __creatureTextPos);

    }

    @Override
    public void draw() {
        EntityManager.get().draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();

        Player player = (Player) EntityManager.get().getPlayer();
        if (player.getPet() != null) {
            _countDownSeconds -= Gdx.graphics.getDeltaTime();
            if (_lastTime != (int) _countDownSeconds) {
                _lastTime = (int) _countDownSeconds;
                _timerText.setMessage(timeDisplay());
            }

            List<Entity> opponents = EntityManager.get().getEntities(EntityTypes.get("Catchable"));
            if (opponents.size() <= 0) {
                StateManager.get().push(new Tournament((Player) EntityManager.get().getPlayer()));
            }

            if (_lastCreatureCount != opponents.size()) {
                _lastCreatureCount = opponents.size();
                _creatureText.setMessage(creatureDisplay(opponents.size()));
            }

            if (_countDownSeconds <= 0 && opponents.size() > 0) {
                StateManager.get().push(new Battle(player.getPet(), ((Catchable) opponents.get(RNG.next(0, opponents.size()))).getPet()));
            }
        }
        else {
            _timerText.setMessage("Catch a creature!");
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        List<Entity> opponents = EntityManager.get().getEntities(EntityTypes.get("Catchable"));

        if (opponents.size() <= 0 && EntityManager.get().getPlayer() != null) {
            StateManager.get().push(new Tournament((Player) EntityManager.get().getPlayer()));
        }
        _countDownSeconds = GameConfig.ArenaTimeoutSeconds;
        if (EntityManager.get().getPlayer() == null) {
            MusicPlayer.get(new SingleSongPlayer("Anticipation.ogg"));
        }
        MusicPlayer.get().start();
    }

    @Override
    public void pause() {
        MusicPlayer.get().stop();
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        MusicPlayer.get().stop();
    }

    @Override
    public String getName() {
        return "World";
    }

    @Override
    public void resize(int width, int height) {
    }
}
