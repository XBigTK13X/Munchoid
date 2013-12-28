package game.arena;

import game.battle.Battle;
import game.battle.TimerGraphic;
import game.core.GameConfig;
import game.core.InputWrapper;
import game.core.Score;
import game.creatures.Creature;
import game.creatures.Merge;
import game.creatures.Stats;
import game.dev.DevConfig;
import game.forces.Force;
import game.tournament.Tournament;
import sps.audio.MusicPlayer;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Screen;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;

import java.util.List;

public class Arena implements State {

    private static final Point2 __timerPos = Screen.pos(5, 75);
    private int _lastTime;
    private CoolDown _battleCountDown;
    private TimerGraphic _timer;

    private static final Point2 __creatureTextPos = Screen.pos(55, 95);
    private Text _creatureText;
    private int _lastCreatureCount;

    private ArenaPayload _preload;

    public Arena(ArenaPayload preload) {
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
        _battleCountDown = new CoolDown(GameConfig.ArenaTimeoutSeconds);
        _lastTime = (int) GameConfig.ArenaTimeoutSeconds;
        _timer = _preload.getTimer();
        _timer.setPosition(__timerPos);
        _timer.setMoveable(false);

        EntityManager.get().addEntity(_preload.getFloor());
        EntityManager.get().addEntity(_preload.getPlayer());
        EntityManager.get().addEntity(_preload.getPlayer().getNet());
        EntityManager.get().addEntity(_preload.getPlayer().getArrow());

        for (Catchable catchable : _preload.getCatchables()) {
            EntityManager.get().addEntity(catchable);
        }

        _creatureText = TextPool.get().write(creatureDisplay(GameConfig.CreatureLimit), __creatureTextPos);
        _creatureText.setMoveable(false);
        if (DevConfig.EndToEndStateLoadTest || DevConfig.TournyTest) {
            _preload.getPlayer().setPet(new Creature());
            if (DevConfig.TournyTest) {
                StateManager.get().push(new Tournament(_preload.getPlayer()));
            }
        }
        EntityManager.get().update();
        StateManager.get().showTutorial();
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
        _timer.draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();

        Player player = (Player) EntityManager.get().getPlayer();
        if (player.getPet() != null) {
            _battleCountDown.update();
            _timer.setPercent(_battleCountDown.getPercentCompletion());
            List<Entity> opponents = EntityManager.get().getEntities(EntityTypes.get("Catchable"));
            if (opponents.size() <= 0) {
                StateManager.get().push(new Tournament((Player) EntityManager.get().getPlayer()));
            }
            else {
                if ((_battleCountDown.isCooled() && opponents.size() > 0) || (InputWrapper.push() && DevConfig.ShortcutsEnabled) || DevConfig.EndToEndStateLoadTest) {
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
    }

    @Override
    public void asyncUpdate() {
    }

    private void simulateCreatureGrowth(List<Entity> opponents) {
        if (DevConfig.UseOldCatchableMergeAlgorithm) {
            if (opponents.size() > 1) {
                for (int ii = 0; ii + 1 < opponents.size(); ii += 2) {
                    if (opponents.get(ii).isActive() && opponents.get(ii + 1).isActive() && RNG.percent(GameConfig.ArenaMergeChance)) {
                        Catchable c1 = (Catchable) opponents.get(ii);
                        Catchable c2 = (Catchable) opponents.get(ii + 1);
                        if (c1.getCreature().getBody().getParts().anyAlive() && c2.getCreature().getBody().getParts().anyAlive()) {
                            Creature merged = Merge.creatures(c1.getCreature(), c2.getCreature());
                            c1.setCreature(merged);
                            opponents.get(ii + 1).setInactive();
                            c1.getCreature().getStats().activateRandom();
                        }
                    }
                }
                EntityManager.get().getPlayer().setLocation(Screen.pos(50, 50));
            }
        }
        else {
            for (Entity e : opponents) {
                Catchable c = (Catchable) e;
                c.getCreature().getStats().grow();
            }
        }
    }

    @Override
    public void load() {
        List<Entity> opponents = EntityManager.get().getEntities(EntityTypes.get("Catchable"));

        if (opponents.size() <= 0 && EntityManager.get().getPlayer() != null) {
            StateManager.get().push(new Tournament((Player) EntityManager.get().getPlayer()));
        }

        simulateCreatureGrowth(opponents);

        if (_battleCountDown != null) {
            _battleCountDown.reset();
        }
        Player p = (Player) EntityManager.get().getPlayer();
        if (p == null) {
            MusicPlayer.get().play("Anticipation");
        }
        else {
            //FIXME this is ugly.
            MusicPlayer.get().play("Quickly", false);
            //Instead, should be a one liner to re-center the camera.
            //However, that would result in a lot of positions being gathered,
            //so the method below was chosen instead to save some time.

            //Recenter the camera on the player
            p.moveInBothDirections(-1, -1);
            p.moveInBothDirections(1, 1);
        }
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
        return "Arena";
    }
}
