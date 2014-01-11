package game.core;

import com.badlogic.gdx.Gdx;
import game.dev.DevConfig;
import game.dev.DevShortcuts;
import game.save.Options;
import game.tutorial.Tutorials;
import sps.bridge.Commands;
import sps.console.DevConsole;
import sps.core.SpsGame;
import sps.display.Window;
import sps.io.Input;
import sps.particles.ParticleWrapper;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.UiElements;

public class LoadedGame extends SpsGame {
    private State _preUpdateState;
    private boolean _firstUpdateOptionsLoaded = false;

    private void handleWindowQuerks() {
        if (!_firstUpdateOptionsLoaded) {
            Options options = Options.load();
            options.apply();
            _firstUpdateOptionsLoaded = true;
        }
    }

    private void handleUserInput() {
        Input.get().update();

        if (InputWrapper.pause() && !DevConsole.get().isActive() && !Tutorials.get().isActive() && !ExitPrompt.get().isActive()) {
            PausePrompt.get().setActive(!PausePrompt.get().isActive());
        }

        if (InputWrapper.fullScreen()) {
            Options options = Options.load();
            options.FullScreen = !Gdx.graphics.isFullscreen();
            options.apply();
            options.save();
        }

        if (!DevConsole.get().isActive()) {
            if (Input.get().isActive(Commands.get("Help"))) {
                Tutorials.get().show(true);
            }

            if (Input.get().isActive(Commands.get("Exit"))) {
                if (ExitPrompt.get().isActive()) {
                    ExitPrompt.get().setActive(false);
                }
                else {
                    if (!Tutorials.get().close()) {
                        ExitPrompt.get().setActive(true);
                    }
                }
            }
        }
    }

    private void nonGameUpdates() {
        if (DevConfig.TestGameFreeze) {
            try {
                Thread.sleep(GameConfig.ThreadMaxStalledMilliseconds + 10000);
            }
            catch (Exception e) {

            }
        }
        GameMonitor.keepAlive();
        handleWindowQuerks();
        handleUserInput();
        PausePrompt.get().updateAndDraw();
        ExitPrompt.get().updateAndDraw();
        Tutorials.get().update();
        DevConsole.get().updateAndDraw();
        DevShortcuts.handle();
    }

    @Override
    public void update() {
        nonGameUpdates();

        if (!PausePrompt.get().isActive() && !ExitPrompt.get().isActive()) {
            _preUpdateState = StateManager.get().current();
            StateManager.get().asyncUpdate();
            StateManager.get().update();
            ParticleWrapper.get().update();
            TextPool.get().update();
            UiElements.get().update();
        }
    }

    @Override
    public void draw() {
        if (_preUpdateState == StateManager.get().current()) {
            if (GameConfig.OptShowFPS) {
                DevConsole.get().add("" + Gdx.graphics.getFramesPerSecond() + ": " + Gdx.graphics.getDeltaTime());
            }

            if (!PausePrompt.get().isActive() && !ExitPrompt.get().isActive()) {
                StateManager.get().draw();
                UiElements.get().draw();
                TextPool.get().draw();
                ParticleWrapper.get().draw();
            }

            Window.processDrawCalls();
        }
    }
}
