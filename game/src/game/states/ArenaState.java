package game.states;

import com.badlogic.gdx.Gdx;
import game.Shared;
import game.creatures.Creature;
import sps.core.Point2;
import sps.entities.EntityManager;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.Screen;

public class ArenaState implements State {
    private static float __countDownSecondsMax = 10f;
    private static Point2 timerPos = Screen.pos(15, 85);
    private int _lastTime;
    private float _countDownSeconds;
    private Text _timerText;

    private String timeDisplay() {
        return _lastTime == 1 ?
                "Time Remaining: " + _lastTime + " second" : "Time Remaining: " + _lastTime + " seconds";
    }

    @Override
    public void create() {
        if (Shared.get().playerCreature() == null) {
            Shared.get().setPlayerCreature(new Creature(false));
        }
        _countDownSeconds = __countDownSecondsMax;
        _lastTime = (int) __countDownSecondsMax;
        _timerText = TextPool.get().write(timeDisplay(), timerPos);
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();

        _countDownSeconds -= Gdx.graphics.getDeltaTime();
        if (_lastTime != (int) _countDownSeconds) {
            _lastTime = (int) _countDownSeconds;
            _timerText.setMessage(timeDisplay());
        }
        if (_countDownSeconds <= 0) {
            StateManager.get().push(new BattleState());
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
    }

    @Override
    public String getName() {
        return "World";
    }

    @Override
    public void resize(int width, int height) {
    }
}
