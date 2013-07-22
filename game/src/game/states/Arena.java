package game.states;

import com.badlogic.gdx.Gdx;
import game.GameConfig;
import game.Score;
import game.arena.Catchable;
import game.arena.Player;
import game.arena.Preload;
import game.creatures.Creature;
import game.creatures.Merge;
import game.creatures.Stats;
import game.forces.Force;
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

    private Preload _preload;

    public Arena(Preload preload) {
        _preload = preload;
    }

    private String timeDisplay() {
        return "Time Remaining: " + _lastTime + " second" + ((_lastTime == 1) ? "" : "s");
    }

    private String creatureDisplay(int creatureCount) {
        return "Creatures remaining: " + creatureCount;
    }

    @Override
    public void create() {
        Score.reset();
        _countDownSeconds = GameConfig.ArenaTimeoutSeconds;
        _lastTime = (int) GameConfig.ArenaTimeoutSeconds;
        _timerText = TextPool.get().write(timeDisplay(), __timerPos);
        _timerText.setMoveable(false);

        EntityManager.get().addEntity(_preload.getFloor());
        EntityManager.get().addEntity(_preload.getPlayer());
        EntityManager.get().addEntity(_preload.getPlayer().getNet());
        EntityManager.get().addEntity(_preload.getPlayer().getArrow());

        for (Catchable catchable : _preload.getCatchables()) {
            EntityManager.get().addEntity(catchable);
        }

        _creatureText = TextPool.get().write(creatureDisplay(GameConfig.CreatureLimit), __creatureTextPos);
        _creatureText.setMoveable(false);
        if (GameConfig.DevEndToEndStateLoadTest) {
            _preload.getPlayer().setPet(new Creature());
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
                if ((_countDownSeconds <= 0 && opponents.size() > 0) || (Input.get().isActive(Commands.get("Push")) && GameConfig.DevShortcutsEnabled) || GameConfig.DevEndToEndStateLoadTest) {
                    Creature opponent = ((Catchable) opponents.get(RNG.next(0, opponents.size()))).getCreature();
                    if (Score.get().victories() == 0) {
                        opponent.setStats(Stats.createWeakling(player.getPet().getStats()));
                        Stats pet = player.getPet().getStats();
                        Force strongest = pet.randomEnabledForce();
                        pet.set(strongest, (int) (pet.get(strongest) * GameConfig.FirstFightMult));
                    }
                    StateManager.get().push(new Battle(player.getPet(), opponent));
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
                        Creature merged = Merge.creatures(c1.getCreature(), c2.getCreature());
                        c1.setCreature(merged);
                        opponents.get(ii + 1).setInactive();
                        c1.getCreature().getStats().activateRandom();
                    }
                }
            }
            EntityManager.get().getPlayer().setLocation(Screen.pos(50, 50));
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
