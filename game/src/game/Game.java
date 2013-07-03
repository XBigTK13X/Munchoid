package game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import game.states.PreGame;
import sps.bridge.Commands;
import sps.bridge.SpriteTypes;
import sps.bridge.Sps;
import sps.core.DevConsole;
import sps.core.Logger;
import sps.core.RNG;
import sps.graphics.FrameStrategy;
import sps.graphics.Renderer;
import sps.graphics.SpriteSheetManager;
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
        Renderer.setAllWindowBackground(Color.BLACK);
        Renderer.setAllStrategy(new FrameStrategy());
        Renderer.setAllRefreshInstance(this);
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());
        StateManager.get().push(new PreGame());
        ParticleEngine.reset();
    }

    @Override
    public void resize(int width, int height) {
        Renderer.resizeAll(width, height);
        StateManager.get().resize(width, height);
    }


    State _preUpdateState;

    @Override
    public void render() {
        try {
            //Logger.devConsole("" + Gdx.graphics.getFramesPerSecond() + ": " + Gdx.graphics.getDeltaTime());

            // Update
            Input.get().update();

            if (Input.get().isActive(Commands.get("ToggleDevConsole"), 0)) {
                DevConsole.get().toggle();
            }
            if (Input.get().isActive(Commands.get("ToggleFullScreen"), 0)) {
                Renderer.get().toggleFullScreen();
            }

            _preUpdateState = StateManager.get().current();
            StateManager.get().asyncUpdate();
            StateManager.get().update();
            ParticleEngine.get().update();
            TextPool.get().update();
            UiElements.get().update();

            if (_preUpdateState == StateManager.get().current()) {
                // Render
                Renderer.get(true).setListening(true);
                Renderer.get().begin();
                StateManager.get().draw();
                ParticleEngine.get().draw();
                UiElements.get().draw();
                TextPool.get().draw();
                DevConsole.get().draw();
                Renderer.get().end();
                Renderer.get(true).processQueue();
            }


        } catch (Exception e) {
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
    }
}
