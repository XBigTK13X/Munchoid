package sps.io;

import sps.bridge.Command;
import sps.bridge.Context;

public class FalseInput implements InputProvider {
    @Override
    public void setup(StateProvider stateProvider) {

    }

    @Override
    public boolean detectState(Command command, int playerIndex) {
        return false;
    }

    @Override
    public boolean isActive(Command command, int playerIndex, boolean failIfLocked) {
        return false;
    }

    @Override
    public boolean isActive(Command command) {
        return false;
    }

    @Override
    public boolean isActive(Command command, int playerIndex) {
        return false;
    }

    @Override
    public void setContext(Context context, int playerIndex) {

    }

    @Override
    public boolean isContext(Context context, int playerIndex) {
        return false;
    }

    @Override
    public boolean isLocked(Command command, int playerIndex) {
        return false;
    }

    @Override
    public void lock(Command command, int playerIndex) {

    }

    @Override
    public void unlock(Command command, int playerIndex) {

    }

    @Override
    public void update() {

    }

    @Override
    public int x() {
        return 0;
    }

    @Override
    public int y() {
        return 0;
    }

    @Override
    public boolean isMouseDown() {
        return false;
    }

    @Override
    public void setMouseLock(boolean locked) {
    }

    @Override
    public boolean isMouseDown(boolean failIfLocked) {
        return false;
    }
}
