package game.test;

import game.arena.Catchable;
import game.battle.BattleHUD;
import game.battle.TimerGraphic;
import game.core.InputWrapper;
import sps.color.Color;
import sps.display.Screen;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Meter;

public class MeterProgressTest implements State {
    private Meter _meter;
    private Text _progress;
    private BattleHUD _hud;
    private TimerGraphic _timer;
    private TimerGraphic _timer2;

    private void regenMeter() {
        _meter = new Meter(40, 10, Color.BLUE, Screen.pos(55, 45), false);
        Catchable c = new Catchable(null, null);
        c.getCreature().setLocation(Screen.pos(30, 15));
        _hud = new BattleHUD(c.getCreature());
        _timer = new TimerGraphic(true, Screen.pos(30, 40), Color.GRAY);
        _timer2 = new TimerGraphic(false, Screen.pos(30, 20), Color.YELLOW);
    }

    @Override
    public void create() {
        _progress = TextPool.get().write("", Screen.pos(30, 70));
        regenMeter();
        _meter.scale(50);
    }

    @Override
    public void draw() {
        _meter.draw();
        _hud.draw();
        _timer.draw();
        _timer2.draw();
    }

    @Override
    public void update() {
        if (InputWrapper.moveRight()) {
            _meter.scale(_meter.getPercent() + 1);
            _timer2.setPercent(_timer2.getPercent() + 1);
            _timer.setPercent(_timer.getPercent() + 1);
            _progress.setMessage("Meter: " + _meter.getPercent() + ", Timer: " + _timer.getPercent());

        }
        if (InputWrapper.moveLeft()) {
            _meter.scale(_meter.getPercent() - 1);
            _timer.setPercent(_timer.getPercent() - 1);
            _timer2.setPercent(_timer2.getPercent() - 1);
            _progress.setMessage("Meter: " + _meter.getPercent() + ", Timer: " + _timer.getPercent());
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

    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void pause() {

    }
}
