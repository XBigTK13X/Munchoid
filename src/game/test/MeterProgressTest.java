package game.test;

import game.InputWrapper;
import game.ui.Meter;
import sps.color.Color;
import sps.display.Screen;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

public class MeterProgressTest implements State {
    private Meter _meter;
    private Text _progress;

    private void regenMeter() {
        _meter = new Meter(80, 10, Color.BLUE, Screen.pos(5, 45), false);
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }
}
