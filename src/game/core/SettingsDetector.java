package game.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.Creature;
import game.pregame.PreloadMainMenu;
import game.save.Options;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.UIButton;
import sps.util.SimpleTimer;

public class SettingsDetector implements State {
    Text _display;

    @Override
    public void create() {
        _display = TextPool.get().write("Press 'Begin' and Munchoid will determine the optimal settings for your machine.", Screen.pos(5, 70));
        _display.setFont("Console", 30);
        UIButton confirm = new UIButton("Begin") {
            @Override
            public void click() {
                _display.setMessage("Determining optimal settings for this machine...");
                Options o = Options.load();
                o.GraphicsLowQuality = false;
                o.apply();
                o.save();
                testStep = 1;
                setVisible(false);
            }
        };
        confirm.setColRow(2, 2);
        confirm.layout();
    }

    private void proceed() {
        StateManager.get().push(new PreloadMainMenu());
    }

    @Override
    public void draw() {

    }

    private int testStep = 0;
    private SimpleTimer timer = new SimpleTimer();
    private boolean creatureQuality;
    private boolean bgQuality;

    @Override
    public void update() {
        if (testStep == 1) {
            timer.start(true);
            Creature creature = new Creature();
            timer.stop();
            creatureQuality = (timer.getElapsedTimeMillis() <= 1000);
            Logger.info("CR: " + timer.getElapsedTimeMillis());
            testStep = 2;
        }
        else if (testStep == 2) {
            timer.start(true);
            Sprite bg = BackgroundCache.createMenuBackground();
            timer.stop();
            bgQuality = timer.getElapsedTimeMillis() <= 1000;
            Logger.info("BG: " + timer.getElapsedTimeMillis());
            testStep = 3;
        }
        else if (testStep == 3) {
            Options options = Options.load();
            options.GraphicsLowQuality = !bgQuality || !creatureQuality;
            options.SettingsDetected = true;
            options.apply();
            options.save();
            testStep = 4;
            _display.setMessage("Graphics mode has been set to " + (options.GraphicsLowQuality ? "FAST" : "PRETTY") + "\nPress " + Commands.get("Confirm") + " to continue");
        }
        else if (testStep == 4) {
            if (InputWrapper.confirm()) {
                proceed();
            }
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
        return null;
    }

    @Override
    public void pause() {

    }
}
