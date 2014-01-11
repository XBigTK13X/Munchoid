package game.stages.battle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.ArenaScore;
import game.app.core.BackgroundCache;
import game.app.core.InputWrapper;
import config.UIConfig;
import game.stages.common.creatures.Creature;
import game.app.dev.DevConfig;
import game.stages.common.forces.Force;
import game.stages.tournament.TournamentEnd;
import game.app.tutorial.Tutorials;
import sps.audio.MusicPlayer;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.core.Logger;
import sps.display.Screen;
import sps.display.Window;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.ui.Tooltips;
import sps.ui.UIButton;

public class Battle implements State {
    private Creature _left;
    private Creature _right;

    private Sprite _background;

    private BattleHUD _leftHud;
    private BattleHUD _rightHud;

    private boolean _isFinalBattle;

    private boolean _isBattleOver;
    private boolean _playerWon;

    private UIButton _passBtn;

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

    public void rebuildHud() {
        _leftHud = new BattleHUD(_left);
        _rightHud = new BattleHUD(_right);
    }

    @Override
    public void create() {
        _background = BackgroundCache.getRandom();

        _left.orientX(false, true);
        _right.orientX(true, true);

        EntityManager.get().addEntity(_right);
        _right.getBody().setScale(UIConfig.BattleCreatureScale);
        _left.getBody().setScale(UIConfig.BattleCreatureScale);
        EntityManager.get().addEntity(_left);
        _right.setOpponent(_left);
        _left.setOpponent(_right);

        rebuildHud();

        _passBtn = new UIButton("Pass") {
            @Override
            public void click() {
                pass();
            }
        };
        _passBtn.setScreenPercent(27, 65);
        _passBtn.setVisible(false);
        _passBtn.setSize(10, 8);
        _passBtn.layout();

        Tutorials.get().show();
    }


    @Override
    public void draw() {
        Window.get().schedule(_background, DrawDepths.get("GameBackground"));
        EntityManager.get().draw();
        _leftHud.draw();
        _rightHud.draw();
    }

    private void playerAttack(Force force) {
        playerShowCost(force);
        if (_left.getCoolDown().isCooled()) {
            attack(_left, force);
        }
    }

    private void attack(Creature creature, Force force) {
        creature.attack(force);
        _rightHud.updateAttackBasedState();
        _leftHud.updateAttackBasedState();
    }

    public void playerActivate(Force force) {
        if (_left.canUse(force)) {
            if (_left.getStats().get(force) > 0) {
                playerAttack(force);
            }
        }
        else {
            TextPool.get().write("Not enough energy", Screen.pos(20, 50), 1f, TextEffects.Fountain);
        }
    }

    @Override
    public void update() {
        EntityManager.get().update();
        _leftHud.update();
        _rightHud.update();

        if (_isBattleOver) {
            if (InputWrapper.confirm() || DevConfig.BotEnabled) {
                if (DevConfig.BattleTest) {
                    StateManager.reset().push(new Battle());
                }
                else {
                    if (_playerWon) {
                        victory();
                    }
                    else {
                        loss();
                    }
                }
            }
        }
        else {
            battleStep();
        }
    }

    private void pass() {
        TextPool.get().write("Recharging...", _passBtn.getPosition().add(0, Screen.height(10)), _left.getCoolDown().getSecondsMax());
        _left.getCoolDown().reset();
        _passBtn.setVisible(false);
    }

    private void battleStep() {
        if (DevConfig.ShortcutsEnabled) {
            if (InputWrapper.pop()) {
                waitForPlayerToAdvance(true);
            }

            if (InputWrapper.push()) {
                waitForPlayerToAdvance(false);
            }
        }

        if (_left.getCoolDown().isCooled()) {
            int usableForces = 0;
            for (Force f : Force.values()) {
                if (_left.canUse(f)) {
                    usableForces++;
                }
            }
            if (usableForces == 0) {
                pass();
            }
            else {
                if (usableForces < _left.getStats().enabledCount()) {
                    _passBtn.setVisible(true);
                    if (InputWrapper.pass()) {
                        pass();
                    }
                }
                else {
                    _passBtn.setVisible(false);
                }
            }
        }

        /*
         This clause is meant to be separate from the check above

         In the above case, player is checked to be cooled.
         If no forces could have been used, then we reset the cooldown.
         This prevents the player from having priority when no forces
         could be used. Instead, let the player know that the creature
         is recharging energy.
        */
        if (_left.getCoolDown().isCooled()) {
            if (DevConfig.BotEnabled) {
                Force max = Force.Abrasive;
                for (Force force : Force.values()) {
                    if (_left.getStats().get(force) > _left.getStats().get(max)) {
                        max = force;
                    }
                }
                if (_left.canUse(max)) {
                    playerActivate(max);
                }
                else {
                    Force f = _left.getStats().randomEnabledForce();
                    playerActivate(f);
                }
            }
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command), 0) && _left.getStats().isEnabled(force)) {
                    playerActivate(force);
                }
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


        if (!_right.getBody().getParts().anyAlive() || DevConfig.EndToEndStateLoadTest) {
            waitForPlayerToAdvance(true);
        }
        if (!_left.getBody().getParts().anyAlive()) {
            waitForPlayerToAdvance(false);
        }
    }

    private void waitForPlayerToAdvance(boolean win) {
        _playerWon = win;
        _isBattleOver = true;
        String battleResult = "The battle is over. You have " + (win ? "won!" : "lost.") + " Press " + Commands.get("Confirm") + " to continue";
        TextPool.get().write(battleResult, Screen.pos(15, 65));
    }

    private void victory() {
        ArenaScore.get().addVictory();
        ArenaScore.get().addHealthRemaining(_left.getBody().getPercentHealth());
        _left.restore();
        StateManager.get().pop();
        if (_isFinalBattle) {
            ArenaScore.get().setPlayerPetStats(_left.getStats());
            StateManager.get().push(new TournamentEnd(true));
        }
        else {
            StateManager.get().push(new MergeOutcome(_left, _right));
        }
    }

    private void loss() {
        ArenaScore.get().setPlayerPetStats(_left.getStats());
        StateManager.get().push(new TournamentEnd(false));
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        MusicPlayer.get().play("BattleTheme");
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        Tooltips.reset();
    }

    @Override
    public String getName() {
        return "Battle";
    }

    @Override
    public void pause() {
    }

    public void playerShowCost(Force force) {
        _leftHud.flashCost(_left.getCostPercent(force));
    }

    public Creature getPlayer() {
        return _left;
    }
}
