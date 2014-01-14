package game.app;

import com.badlogic.gdx.Gdx;
import game.app.config.GameConfig;
import game.app.core.InputWrapper;
import game.app.dev.DevConfig;
import game.app.dev.DevShortcuts;
import game.app.prompts.ExitPrompt;
import game.app.prompts.PausePrompt;
import game.app.save.Options;
import game.app.tutorial.Tutorials;
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
import sps.util.GameMonitor;

public class LoadedGame extends SpsGame {
    private State _preUpdateState;
    private boolean _firstUpdateOptionsLoaded = false;

    private void handleWindowQuerks() {
        if (!_firstUpdateOptionsLoaded) {
            Options.load();
            Options.get().apply();
            _firstUpdateOptionsLoaded = true;
        }
    }

    private void handleUserInput() {
        Input.get().update();

        if (InputWrapper.pause() && !DevConsole.get().isActive() && !Tutorials.get().isActive() && !ExitPrompt.get().isActive()) {
            PausePrompt.get().setActive(!PausePrompt.get().isActive());
        }

        if (InputWrapper.fullScreen() && !DevConsole.get().isActive()) {
            Options.get().FullScreen = !Gdx.graphics.isFullscreen();
            Options.get().apply();
            Options.get().save();
        }

        if (!DevConsole.get().isActive()) {
            if (Input.get().isActive(Commands.get("Help"))) {
                Tutorials.get().show(true);
            }

            if (Input.get().isActive(Commands.get("Exit"))) {
                if (ExitPrompt.get().isActive()) {
                    ExitPrompt.get().close();
                }
                else {
                    if (!Tutorials.get().close()) {
                        ExitPrompt.get().activate();
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
