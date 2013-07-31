package game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import game.states.AnimationTest;
import sps.bridge.Commands;
import sps.bridge.SpriteTypes;
import sps.bridge.Sps;
import sps.core.DevConsole;
import sps.core.Logger;
import sps.core.RNG;
import sps.display.FrameStrategy;
import sps.display.SpriteSheetManager;
import sps.display.Window;
import sps.io.DefaultStateProvider;
import sps.io.Input;
import sps.particles.ParticleEngine;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.UiElements;

public class Game implements ApplicationListener {
    @Override
    public void create() {
        RNG.seed((int) System.currentTimeMillis());
        Sps.setup();
        Window.setWindowBackground(Color.BLACK);
        Window.get(false).setStrategy(new FrameStrategy());
        Window.get(true).setStrategy(new FrameStrategy());
        Window.setRefreshInstance(this);
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());
        StateManager.get().push(new AnimationTest());
        ParticleEngine.reset();
        StateManager.get().setPaused(false);
    }

    @Override
    public void resize(int width, int height) {
        Window.resize(width, height);
        StateManager.get().resize(width, height);
    }


    State _preUpdateState;

    private void update() {
        Input.get().update();

        if (Input.get().isActive(Commands.get("ToggleDevConsole"), 0)) {
            DevConsole.get().toggle();
        }
        if (Input.get().isActive(Commands.get("ToggleFullScreen"), 0)) {
            Window.get().toggleFullScreen();
        }

        //TODO Remove debugging helper
        if (Input.get().isActive(Commands.get(CommandNames.Debug))) {
            MetaData.printWin();
        }

        if (Input.get().isActive(Commands.get(CommandNames.Pause))) {
            StateManager.get().setPaused(!StateManager.get().isPaused());
        }

        if (!StateManager.get().isPaused()) {
            _preUpdateState = StateManager.get().current();
            StateManager.get().asyncUpdate();
            StateManager.get().update();
            ParticleEngine.get().update();
            TextPool.get().update();
            UiElements.get().update();
        }
    }

    private void draw() {
        if (_preUpdateState == StateManager.get().current()) {
            if (GameConfig.OptShowFPS) {
                Logger.devConsole("" + Gdx.graphics.getFramesPerSecond() + ": " + Gdx.graphics.getDeltaTime());
            }
            Window.clear();
            Window.get(true).setListening(true);

            Window.get().begin();

            StateManager.get().draw();
            ParticleEngine.get().draw();
            UiElements.get().draw();
            TextPool.get().draw();
            DevConsole.get().draw();

            Window.get().end();

            Window.get(false).processQueues();
            Window.get(true).processQueues();
        }
    }

    @Override
    public void render() {
        try {
            Logger.devConsole("" + Gdx.graphics.getFramesPerSecond() + ": " + Gdx.graphics.getDeltaTime());
            update();
            draw();
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    public class DrawDepths {
        public final static String Atom = "Atom";
    }

    public class CommandNames {
        public final static String MoveUp = "MoveUp";
        public final static String MoveDown = "MoveDown";
        public final static String MoveLeft = "MoveLeft";
        public final static String MoveRight = "MoveRight";
        public final static String Debug = "Debug";
        public final static String Pause = "Pause";
    }
}
