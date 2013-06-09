package game.states;

import game.GameConfig;
import game.battle.ForcesHUD;
import game.battle.HealthMeter;
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
    private boolean _isPlayerTurn = true;
    private Creature _left;
    private Creature _right;
    private ForcesHUD _leftUI;
    private ForcesHUD _rightUI;
    private HealthMeter _leftHealth;
    private HealthMeter _rightHealth;

    public Battle() {
        this(new Creature(), new Creature());
        Logger.error("ONLY USE THIS METHOD FOR DEBUGGING");
    }

    public Battle(Creature slot1, Creature slot2) {
        _left = slot1;
        _right = slot2;
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

        _leftUI = new ForcesHUD(_left);
        _rightUI = new ForcesHUD(_right);

        _leftHealth = new HealthMeter(_left);
        _rightHealth = new HealthMeter(_right);

        TextPool.get().write(_left.getName(), Screen.pos(0, 50).add((int) _left.getLocation().X, 0));
        TextPool.get().write(_right.getName(), Screen.pos(0, 50).add((int) _right.getLocation().X, 0));
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
        //TODO makes these proper entities to enable depth sorting
        _leftUI.draw();
        _rightUI.draw();
        _leftHealth.draw();
        _rightHealth.draw();
    }

    public void playerAttack(Force force) {
        if (_isPlayerTurn) {
            _left.attack(force);
            _isPlayerTurn = false;
        }
    }

    @Override
    public void update() {
        EntityManager.get().update();
        _leftHealth.update();
        _rightHealth.update();

        if (_isPlayerTurn) {
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command), 0) && _left.getStats().isEnabled(force)) {
                    if (_left.getStats().get(force) > 0) {
                        playerAttack(force);
                    }
                    else {
                        TextPool.get().write(force.name() + " Disabled", Screen.pos(10, 50), 1f, TextEffects.Fountain);
                    }
                }
            }
            if (Input.get().isActive(Commands.get("Pop"))) {
                //TODO Remove this debugging helper
                victory();
            }
        }
        else {
            //TODO Smarter attacks
            _right.attack(_right.getStats().randomEnabledForce());
            _isPlayerTurn = true;
        }

        if (!_right.getBody().isAlive() || GameConfig.PlaythroughTest) {
            victory();
        }
        if (!_left.getBody().isAlive()) {
            StateManager.reset().push(new GameLose());
        }
    }

    private void victory() {
        _left.getBody().restore();
        StateManager.get().pop();
        StateManager.get().push(new MergeOutcome(_left, _right));
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
