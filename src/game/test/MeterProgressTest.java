package game.test;

import game.InputWrapper;
import game.arena.Catchable;
import game.battle.BattleHUD;
import game.ui.Meter;
import sps.color.Color;
import sps.display.Screen;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

public class MeterProgressTest implements State {
    private Meter _meter;
    private Text _progress;
    private BattleHUD _hud;

    private void regenMeter() {
        _meter = new Meter(40, 10, Color.BLUE, Screen.pos(55, 45), false);
        Catchable c = new Catchable(null, null);
        c.getCreature().setLocation(Screen.pos(30, 15));
        _hud = new BattleHUD(c.getCreature());
    }

    @Override
    public void create() {
        _progress = TextPool.get().write("", Screen.pos(10, 80));
        regenMeter();
        _meter.scale(50);
    }

    @Override
    public void draw() {
        _meter.draw();
        _hud.draw();
    }

    @Override
    public void update() {
        if (InputWrapper.moveRight()) {
            _meter.scale(_meter.getPercent() + 1);
            _progress.setMessage("Progress: " + _meter.getPercent());
        }
        if (InputWrapper.moveLeft()) {
            _meter.scale(_meter.getPercent() - 1);
            _progress.setMessage("Progress: " + _meter.getPercent());
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
