package game.states;

import game.GameConfig;
import game.Score;
import game.battle.BattleHUD;
import game.creatures.Creature;
import game.forces.Force;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.ui.ToolTip;
import sps.util.Screen;

public class Battle implements State {
    private static SingleSongPlayer __battleMusic;
    private Creature _left;
    private Creature _right;

    private BattleHUD _leftHud;
    private BattleHUD _rightHud;

    private boolean _isFinalBattle;

    public Battle() {
        this(new Creature(), new Creature());
        Logger.error("ONLY USE Battle() FOR DEBUGGING!!!");
    }

    public Battle(Creature slot1, Creature slot2) {
        _left = slot1;
        _right = slot2;
    }

    public Battle(Creature slot1, Creature slot2, boolean isFinalBattle) {
        this(slot1, slot2);
        _isFinalBattle = isFinalBattle;
    }

    @Override
    public void create() {
        _left.orientX(false, true);
        _right.orientX(true, true);

        EntityManager.get().addEntity(_right);
        _right.getBody().setScale(1);
        _left.getBody().setScale(1);
        EntityManager.get().addEntity(_left);
        _right.setOpponent(_left);
        _left.setOpponent(_right);

        _leftHud = new BattleHUD(_left, true);
        _rightHud = new BattleHUD(_right, false);

    }


    @Override
    public void draw() {
        EntityManager.get().draw();
        _leftHud.draw();
        _rightHud.draw();
    }

    public void playerAttack(Force force) {
        if (_left.getCoolDown().isCooled()) {
            attack(_left, force);
        }
    }

    private void attack(Creature creature, Force force) {
        creature.attack(force);
        _rightHud.updateAttackBasedState();
        _leftHud.updateAttackBasedState();
    }

    private void playerActivate(Force force) {
        if (_left.canUse(force)) {
            if (_left.getStats().get(force) > 0) {
                playerAttack(force);
            }
            else {
                TextPool.get().write(force.name() + " Disabled", Screen.pos(10, 50), 1f, TextEffects.Fountain);
            }
        }
        else {
            TextPool.get().write("Not enough energy", Screen.pos(10, 50), 1f, TextEffects.Fountain);
            //TODO A more skillful means of waiting
            _left.getCoolDown().reset();
        }
    }

    @Override
    public void update() {
        EntityManager.get().update();
        _leftHud.update();
        _rightHud.update();

        if (_left.getCoolDown().isCooled()) {
            if (GameConfig.DevBotEnabled) {
                Force f = _left.getStats().randomEnabledForce();
                playerActivate(f);
            }
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command), 0) && _left.getStats().isEnabled(force)) {
                    playerActivate(force);
                }
            }
            if (Input.get().isActive(Commands.get("Pop")) && GameConfig.DevShortcutsEnabled) {
                victory();
            }
        }
        else if (_right.getCoolDown().isCooled()) {
            //TODO Smarter attacks
            attack(_right, _right.getStats().randomEnabledForce());
        }
        else {
            _right.regenEnergy();
            _left.regenEnergy();
            _right.getCoolDown().update();
            _left.getCoolDown().update();
        }


        if (!_right.getBody().isAlive() || GameConfig.DevEndToEndStateLoadTest) {
            victory();
        }
        if (!_left.getBody().isAlive()) {
            Score.get().setPlayerPetStats(_left.getStats());
            StateManager.reset().push(new GameLose());
        }
    }

    private void victory() {
        Score.get().addVictory();
        Score.get().addHealthRemaining(_left.getBody().getPercentHealth());
        _left.getBody().restore();
        StateManager.get().pop();
        if (_isFinalBattle) {
            Score.get().setPlayerPetStats(_left.getStats());
            StateManager.get().push(new GameWin());
        }
        else {
            StateManager.get().push(new MergeOutcome(_left, _right));
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        if (__battleMusic == null) {
            __battleMusic = new SingleSongPlayer("BattleTheme.ogg");
        }
        MusicPlayer.get(__battleMusic);
        MusicPlayer.get().start();
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        ToolTip.reset();
    }

    @Override
    public String getName() {
        return "Battle";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
