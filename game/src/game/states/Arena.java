package game.states;

import com.badlogic.gdx.Gdx;
import game.GameConfig;
import game.arena.Catchable;
import game.arena.Floor;
import game.arena.Player;
import game.creatures.Creature;
import game.creatures.Merge;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.io.Input;
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
        Floor floor = new Floor();
        EntityManager.get().addEntity(floor);
        Player player = new Player(floor);
        EntityManager.get().addEntity(player);
        for (int ii = 0; ii < GameConfig.CreatureLimit; ii++) {
            EntityManager.get().addEntity(new Catchable());
        }
        _creatureText = TextPool.get().write(creatureDisplay(GameConfig.CreatureLimit), __creatureTextPos);

        if (GameConfig.PlaythroughTest) {
            player.setPet(new Creature());
        }
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
            else {
                if (_countDownSeconds <= 0 && opponents.size() > 0 || Input.get().isActive(Commands.get("Push")) || GameConfig.PlaythroughTest) {
                    StateManager.get().push(new Battle(player.getPet(), ((Catchable) opponents.get(RNG.next(0, opponents.size()))).getCreature()));
                }
            }


            if (_lastCreatureCount != opponents.size()) {
                _lastCreatureCount = opponents.size();
                _creatureText.setMessage(creatureDisplay(opponents.size()));
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

        if (opponents.size() > 1) {
            for (int ii = 0; ii + 1 < opponents.size(); ii += 2) {
                if (opponents.get(ii).isActive() && opponents.get(ii + 1).isActive() && RNG.percent(GameConfig.ArenaMergeChance)) {
                    Catchable c1 = (Catchable) opponents.get(ii);
                    Catchable c2 = (Catchable) opponents.get(ii + 1);
                    if (c1.getCreature().getBody().isAlive() && c2.getCreature().getBody().isAlive()) {
                        Creature merged = Merge.two(c1.getCreature(), c2.getCreature());
                        c1.setCreature(merged);
                        opponents.get(ii + 1).setInactive();
                    }
                }
            }
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
