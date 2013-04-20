package game.states;

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
import sps.util.Screen;

public class Battle implements State {
    private static final Point2 __petLocation = Screen.pos(15, 15);
    private static SingleSongPlayer __battleMusic;
    private boolean _isPlayerTurn = true;
    private Creature _left;
    private Creature _right;

    public Battle(Creature slot1, Creature slot2) {
        _left = slot1;
        _right = slot2;
    }

    @Override
    public void create() {
        EntityManager.get().addEntity(_right);
        _right.getBody().setScale(1);
        _left.setLocation(__petLocation);
        _left.getBody().setScale(1);
        EntityManager.get().addEntity(_left);

        _right.setOpponent(_left);
        _left.setOpponent(_right);


        if (__battleMusic == null) {
            __battleMusic = new SingleSongPlayer("BattleTheme.ogg");
        }
        MusicPlayer.get(__battleMusic);
        MusicPlayer.get().start();
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();

        if (_isPlayerTurn) {
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command), 0)) {
                    _left.attack(force);
                    _isPlayerTurn = false;
                }
            }
            if (Input.get().isActive(Commands.get("Confirm"))) {
                //TODO Remove this debugging helper
                victory();
            }
        }
        else {
            //TODO Smarter attacks
            _right.attack(Force.random());
            _isPlayerTurn = true;
        }

        if (!_right.getBody().isAlive()) {
            victory();
        }
        if (!_left.getBody().isAlive()) {
            StateManager.reset().push(new GameLose());
        }
    }

    private void victory() {
        _right.setInactive();
        EntityManager.get().removeEntity(_right);
        _left.getBody().restore();
        StateManager.get().pop();
        StateManager.get().push(new MergeOutcome(_left, _right));
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        MusicPlayer.get().stop();
    }

    @Override
    public String getName() {
        return "Battle";
    }

    @Override
    public void resize(int width, int height) {
    }
}
