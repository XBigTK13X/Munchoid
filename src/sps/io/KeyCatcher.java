package sps.io;

import com.badlogic.gdx.InputProcessor;
import sps.bridge.Sps;

public abstract class KeyCatcher implements InputProcessor {
    private InputProcessor originalInputProcessor;
    public void activate(){
        Input.disable();

        originalInputProcessor = Sps.getApp().getInput().getInputProcessor();
        Sps.getApp().getInput().setInputProcessor(this);
    }

    public void deactivate(){
        Sps.getApp().getInput().setInputProcessor(originalInputProcessor);
        Input.enable();
    }

    public abstract void onDown(int keyCode);

    @Override
    public boolean keyDown(int i) {
        onDown(i);
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
