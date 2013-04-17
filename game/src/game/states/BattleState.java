package game.states;

import game.arena.Player;
import game.creatures.Creature;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.util.Screen;

public class BattleState implements State {
    private static SingleSongPlayer __battleMusic;

    private static final Point2 __creatureMinDimension = Screen.pos(15, 15);
    private static final Point2 __creatureMaxDimension = Screen.pos(40, 40);

    private static final Point2 __petLocation = Screen.pos(15, 15);

    private boolean _isPlayerTurn = true;
    private Creature _opponent;

    private Player _player;

    public BattleState(Player player) {
        _player = player;
    }

    @Override
    public void create() {
        _opponent = new Creature(true, __creatureMinDimension, __creatureMaxDimension);
        EntityManager.get().addEntity(_opponent);
        _player.getPet().setLocation(__petLocation);
        EntityManager.get().addEntity(_player.getPet());

        _opponent.setOpponent(_player.getPet());
        _player.getPet().setOpponent(_opponent);


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
            if (Input.get().isActive(Commands.get("Force"), 0)) {
                _player.getPet().attack();
                _isPlayerTurn = false;

            }
        }
        else {
            _opponent.attack();
            _isPlayerTurn = true;
        }

        if (!_opponent.getBody().isAlive()) {
            EntityManager.get().removeEntity(_opponent);
            StateManager.get().pop();
            StateManager.get().push(new MergeState(_player.getPet(), _opponent));
        }
        if (!_player.getPet().getBody().isAlive()) {
            StateManager.reset().push(new ArenaState());
        }
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
