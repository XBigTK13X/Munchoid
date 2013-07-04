package sps.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import sps.bridge.DrawDepths;
import sps.bridge.Sps;
import sps.core.Logger;
import sps.core.Point2;
import sps.graphics.Window;
import sps.states.State;
import sps.states.StateManager;

public class Text {
    public static final float NotTimed = Float.MIN_VALUE;

    private State _createdDuring;

    private boolean _canMove;
    private Point2 position;
    private String message;
    private float scale;
    private boolean visible = false;
    private float lifeInSeconds;
    private TextEffect effect;
    private float xVel;
    private float yVel;
    private float dX;
    private float dY;
    private Color _color;

    public Text() {
        _color = Color.WHITE;
        position = new Point2(0, 0);
        _canMove = true;
    }

    public void reset(Point2 position, String message, float scale, float lifeInSeconds, TextEffect effect) {
        _canMove = true;
        _createdDuring = StateManager.get().current();
        this.position.copy(position);
        this.message = message;
        this.scale = scale;
        visible = true;
        this.lifeInSeconds = lifeInSeconds;
        this.effect = effect;
        effect.init(this);
    }

    public void hide() {
        visible = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void update() {
        if (lifeInSeconds != NotTimed && (position.X != 0 || position.Y != 0)) {
            effect.update(this);
            lifeInSeconds -= Gdx.graphics.getDeltaTime();
            if (lifeInSeconds <= 0) {
                visible = false;
            }
        }
    }

    public void draw() {
        Logger.info("Writing: " + message + " at " + position);
        Window.get(!_canMove).draw(message, position, _color, scale, DrawDepths.get(Sps.DrawDepths.Default_Text));
    }

    public boolean isVisible() {
        return visible && isCurrent();
    }

    public void setVel(float x, float y) {
        xVel = x;
        yVel = y;
    }

    public void accel() {
        xVel += dX;
        yVel += dY;
        position.reset(position.PosX + xVel, position.PosY + yVel, false);
    }

    public void setAccel(float dX, float dY) {
        this.dX = dX;
        this.dY = dY;
    }

    public void setColor(Color color) {
        _color = color;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean createdDuring(State state) {
        return _createdDuring == state;
    }

    public boolean isCurrent() {
        return _createdDuring == StateManager.get().current();
    }

    public void setPosition(int x, int y) {
        position.reset(x, y);
    }

    public void setMoveable(boolean moveable) {
        _canMove = moveable;
    }
}
