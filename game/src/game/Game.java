package game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;

import game.states.DefaultState;
import sps.bridge.SpriteTypes;
import sps.bridge.Sps;
import sps.core.DevConsole;
import sps.core.Logger;
import sps.graphics.FrameStrategy;
import sps.graphics.Renderer;
import sps.graphics.SpriteSheetManager;
import sps.io.DefaultStateProvider;
import sps.io.Input;
import sps.particles.ParticleEngine;
import sps.states.StateManager;
import sps.text.TextPool;

public class Game implements ApplicationListener {

    @Override
    public void create() {
        Sps.setup();
        Renderer.get().setWindowsBackground(Color.BLACK);
        Renderer.get().setStrategy(new FrameStrategy());
        Renderer.get().setRefreshInstance(this);
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());
        StateManager.loadState(new DefaultState());
        ParticleEngine.reset();        
        StateManager.loadContent();
    }

    @Override
    public void resize(int width, int height) {
        Renderer.get().resize(width, height);
        StateManager.resize(width, height);
    }

    @Override
    public void render() {
        try {
            //$$$ Logger.devConsole("" + Gdx.graphics.getFramesPerSecond() + ": " + Gdx.graphics.getDeltaTime());

            // Update
            Input.get().update();
            if (Input.get().isActive(sps.bridge.Commands.get("ToggleDevConsole"), 0)) {
                DevConsole.get().toggle();
            }
            if (Input.get().isActive(sps.bridge.Commands.get("ToggleFullScreen"), 0)) {
                Renderer.get().toggleFullScreen();
            }

            StateManager.asyncUpdate();
            StateManager.update();
            ParticleEngine.update();
            TextPool.get().update();

            // Render
            Renderer.get().begin();
            StateManager.draw();
            ParticleEngine.draw();
            TextPool.get().draw();
            DevConsole.get().draw();
            Renderer.get().end();
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
}
