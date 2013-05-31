package game.creatures;

import com.badlogic.gdx.graphics.Color;
import sps.core.RNG;

public class Atom {
    public static int count = 0;
    private Color _color;
    private int survivalChance = 5;
    private float _localX;
    private float _localY;
    private boolean _isActive = true;

    public Atom(int localX, int localY, Color color) {
        count++;
        _localX = localX;
        _localY = localY;
        _color = color;
    }

    public Atom(Atom atom) {
        count++;
        _color = atom.getColor();
        _localX = atom.getLocalX();
        _localY = atom.getLocalY();
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean active) {
        _isActive = active;
    }

    public boolean isLucky() {
        return RNG.percent(survivalChance);
    }

    public void setColor(Color color) {
        _color = color;
    }

    public Color getColor() {
        return _color;
    }

    public float getLocalX() {
        return _localX;
    }

    public float getLocalY() {
        return _localY;
    }
}
