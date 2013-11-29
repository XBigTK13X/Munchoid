package game.ui;

import com.badlogic.gdx.graphics.Color;
import sps.core.Point2;
import sps.io.Input;

public abstract class UISlider {
    private Meter _meter;
    private UIButton _knob;
    private int _height;
    private int _width;
    private Point2 _position;

    public UISlider(int widthPercent, int heightPercent, int x, int y) {
        _meter = new Meter(widthPercent, heightPercent, Color.WHITE, new Point2(x, y), false);
        _width = _meter.getBounds().Width;
        _height = _meter.getBounds().Height;
        _position = new Point2(x, y);

        //Change so that mouseDown detection is on meter, not knob
        _knob = new UIButton("") {
            @Override
            public void click() {
            }

            @Override
            public void mouseDown() {
                if (beingClicked()) {
                    if (Input.get().x() >= _position.X && Input.get().x() <= _position.X + _width) {
                        _knob.setXY(Input.get().x() - _knob.getWidth() / 2, (int) _knob.getPosition().Y);
                        onSlide();
                    }
                }
            }
        };
        _knob.setSize(heightPercent, heightPercent);
        _knob.setXY((int) _position.X + _width / 2, (int) _position.Y);
    }

    public abstract void onSlide();

    public void draw() {
        _meter.draw();
        _knob.draw();
    }

    public int getSliderPercent() {
        return (int) (((_knob.getPosition().X - _position.X) / (float) _width) * 100);
    }
}
