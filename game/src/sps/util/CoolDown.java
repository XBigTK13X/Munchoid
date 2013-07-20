package sps.util;

import com.badlogic.gdx.Gdx;

public class CoolDown {
    private float _coolDown;
    private float _coolDownMax;

    public CoolDown(float lengthInSeconds) {
        _coolDownMax = lengthInSeconds;
        _coolDown = _coolDownMax;
    }

    public boolean isCool() {
        _coolDown -= Gdx.graphics.getDeltaTime();
        boolean result = false;
        if (_coolDown <= 0) {
            result = true;
            _coolDown = _coolDownMax;
        }
        return result;
    }

    public void reset(float lengthInSeconds) {
        _coolDown = lengthInSeconds;
        _coolDownMax = lengthInSeconds;
    }
}
