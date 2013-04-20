package game.states;

import game.arena.Player;
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
    private static SingleSongPlayer __battleMusic;

    private static final Point2 __petLocation = Screen.pos(15, 15);

    private boolean _isPlayerTurn = true;
    private Creature _left;
    private Creature _right;

    public Battle(Player player) {
        _left = player.getPet();
        _right = new Creature();
    }

    public Battle(Creature slot1, Creature slot2) {
        _left = slot1;
        _right = slot2;
    }

    @Override
    public void create() {
        EntityManager.get().addEntity(_right);
        _left.setLocation(__petLocation);
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
        EntityManager.get().removeEntity(_right);
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
