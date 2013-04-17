package game.states;

import com.badlogic.gdx.Gdx;
import game.Shared;
import game.arena.Catchable;
import game.arena.Player;
import sps.core.Point2;
import sps.entities.EntityManager;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Screen;

public class ArenaState implements State {
    private static final float __countDownSecondsMax = 10f;
    private static final Point2 __timerPos = Screen.pos(5, 95);
    private static final int __creatureCount = 15;

    private int _lastTime;
    private float _countDownSeconds;
    private Text _timerText;

    private String timeDisplay() {
        return _lastTime == 1 ?
                "Time Remaining: " + _lastTime + " second" : "Time Remaining: " + _lastTime + " seconds";
    }

    @Override
    public void create() {
        _countDownSeconds = __countDownSecondsMax;
        _lastTime = (int) __countDownSecondsMax;
        _timerText = TextPool.get().write(timeDisplay(), __timerPos);
        EntityManager.get().addEntity(new Player());
        for (int ii = 0; ii < __creatureCount; ii++) {
            EntityManager.get().addEntity(new Catchable());
        }
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();

        if (Shared.get().playerCreature() != null) {
            _countDownSeconds -= Gdx.graphics.getDeltaTime();
            if (_lastTime != (int) _countDownSeconds) {
                _lastTime = (int) _countDownSeconds;
                _timerText.setMessage(timeDisplay());
            }
            if (_countDownSeconds <= 0) {
                StateManager.get().push(new BattleState());
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
        _countDownSeconds = __countDownSecondsMax;
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
    }

    @Override
    public String getName() {
        return "World";
    }

    @Override
    public void resize(int width, int height) {
    }
}
