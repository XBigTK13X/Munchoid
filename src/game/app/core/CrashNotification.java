package game.app.core;

import game.app.InitialStateResolver;
import org.apache.commons.io.FileUtils;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.io.File;

public class CrashNotification implements State {
    private File _notice;

    public CrashNotification(File notice) {
        _notice = notice;
    }

    @Override
    public void create() {
        String message = "";
        try {
            message = FileUtils.readFileToString(_notice);
            FileUtils.forceDelete(_notice);
        }
        catch (Exception e) {
            message = "A previous crash was detected, but a reason for the crash was not found.";
        }
        String proceed = "\n\nPress " + Commands.get("Confirm") + " to continue";
        Text display = TextPool.get().write(message + proceed, Screen.pos(10, 80));
        display.setFont("Console", 30);
    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            StateManager.get().push(InitialStateResolver.create());
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
        return "Crash Notification";
    }

    @Override
    public void pause() {

    }
}
