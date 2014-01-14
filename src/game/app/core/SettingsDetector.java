package game.app.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.InitialStateResolver;
import game.app.config.GameConfig;
import game.app.save.Options;
import game.stages.common.creatures.Creature;
import sps.bridge.Commands;
import sps.color.Color;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.ui.MultiText;
import sps.util.SimpleTimer;

public class SettingsDetector implements State {
    private MultiText _display;

    @Override
    public void create() {
        _display = new MultiText(Screen.pos(5, 5), 15, Color.GRAY.newAlpha(.5f), (int) Screen.width(90), (int) Screen.height(90));
        _display.setFont("Console", 40);
        testStep = 0;
    }

    private void proceed() {
        StateManager.get().push(InitialStateResolver.create());
    }

    @Override
    public void draw() {

    }

    private int testStep = -1;
    private SimpleTimer timer = new SimpleTimer();
    private boolean creatureQuality;
    private boolean bgQuality;

    @Override
    public void update() {
        if (testStep == 0) {
            _display.add("Determining optimal settings for this machine...");
            Options.get().GraphicsLowQuality = false;
            Options.get().apply();
            Options.get().save();
            testStep = 1;
        }
        else if (testStep == 1) {
            timer.start(true);
            Creature creature = new Creature();
            timer.stop();
            creatureQuality = (timer.getElapsedTimeMillis() <= GameConfig.PrettyObjectGenerationThresholdMilliseconds);
            _display.add("PRETTY creature generation speed is " + timer.getElapsedTimeMillis() + " milliseconds.");
            _display.add("Threshold is " + GameConfig.PrettyObjectGenerationThresholdMilliseconds + " milliseconds.");
            testStep = 2;
        }
        else if (testStep == 2) {
            timer.start(true);
            Sprite bg = BackgroundCache.createMenuBackground();
            timer.stop();
            bgQuality = timer.getElapsedTimeMillis() <= GameConfig.PrettyObjectGenerationThresholdMilliseconds;
            _display.add("PRETTY background generation speed is " + timer.getElapsedTimeMillis() + " milliseconds.");
            _display.add("Threshold is " + GameConfig.PrettyObjectGenerationThresholdMilliseconds + " milliseconds.");
            testStep = 3;
        }
        else if (testStep == 3) {
            Options.get().GraphicsLowQuality = !bgQuality || !creatureQuality;
            Options.get().SettingsDetected = true;
            Options.get().apply();
            Options.get().save();
            testStep = 4;
            _display.add("Graphics mode has been set to \"" + (Options.get().GraphicsLowQuality ? "FAST" : "PRETTY") + "\".");
            _display.add("Press " + Commands.get("Confirm") + " to start the game.");
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
