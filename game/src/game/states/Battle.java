package game.states;

import game.battle.ForcesHUD;
import game.creatures.Creature;
import game.forces.Force;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Screen;

public class Battle implements State {
    private static final Point2 __petLocation = Screen.pos(15, 15);
    private static SingleSongPlayer __battleMusic;
    private boolean _isPlayerTurn = true;
    private Creature _left;
    private Creature _right;
    private ForcesHUD _leftUI;
    private ForcesHUD _rightUI;

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
        _left.setLocation(__petLocation);
        _left.getBody().setScale(1);
        EntityManager.get().addEntity(_left);

        _right.setOpponent(_left);
        _left.setOpponent(_right);

        _leftUI = new ForcesHUD(_left);
        _rightUI = new ForcesHUD(_right);
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
        //TODO makes these proper entities to enable depth sorting
        _leftUI.draw();
        _rightUI.draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();

        if (_isPlayerTurn) {
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command), 0)) {
                    if (_left.getStats().get(force) > 0) {
                        _left.attack(force);
                        _isPlayerTurn = false;
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
            _right.attack(_right.getStats().nonZeroForce());
            _isPlayerTurn = true;
        }

        _leftUI.update();

        if (!_right.getBody().isAlive()) {
            victory();
        }
        if (!_left.getBody().isAlive()) {
            StateManager.reset().push(new GameLose());
        }
    }

    private void victory() {
        _right.getBody().kill();
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
