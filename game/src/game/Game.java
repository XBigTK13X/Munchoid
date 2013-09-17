package game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import game.battle.Battle;
import game.creatures.PartFunction;
import game.population.PopulationOverview;
import game.pregame.MainMenu;
import game.save.Options;
import sps.bridge.SpriteTypes;
import sps.bridge.Sps;
import sps.core.DevConsole;
import sps.core.Logger;
import sps.core.RNG;
import sps.display.Assets;
import sps.display.SpriteSheetManager;
import sps.display.Window;
import sps.display.render.FrameStrategy;
import sps.io.DefaultStateProvider;
import sps.io.Input;
import sps.particles.ParticleEngine;
import sps.particles.ParticleWrapper;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.UiElements;

public class Game implements ApplicationListener {
    @Override
    public void create() {
        RNG.seed((int) System.currentTimeMillis());
        Sps.setup();

        Assets.get().fontPack().setDefault("Economica-Regular.ttf", 60);
        Assets.get().fontPack().cacheFont("keys", "Keycaps Regular.ttf", 30);
        Assets.get().fontPack().cacheFont("UIButton", "verdanab.ttf", 70);

        Window.setWindowBackground(Color.BLACK);
        Window.get(false).setStrategy(new FrameStrategy());
        Window.get(true).setStrategy(new FrameStrategy());
        Window.setRefreshInstance(this);
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());

        PartFunction.initJointSpecs();

        State start;
        if (GameConfig.DevPopulationTest) {
            start = new PopulationOverview();
        }
        else if (GameConfig.DevBattleTest) {
            start = new Battle();
        }
        else {
            start = new MainMenu();
        }

        StateManager.get().push(start);
        ParticleEngine.reset();
        StateManager.get().setPaused(false);

        Options.load().apply();
    }

    @Override
    public void resize(int width, int height) {
        Window.resize(width, height, false);
        StateManager.get().resize(width, height);
    }

    State _preUpdateState;

    private void update() {
        Input.get().update();

        if (GameConfig.DevShortcutsEnabled) {
            if (InputWrapper.moveDown() && InputWrapper.moveUp()) {
                StateManager.reset().push(new MainMenu());
            }
            if (InputWrapper.fullScreen()) {
                Options options = Options.load();
                Window.toggleFullScreen(!options.FullScreen, options.WindowResolutionX, options.WindowResolutionY);
            }
            if (InputWrapper.devConsole()) {
                DevConsole.get().toggle();
            }
        }

        if (InputWrapper.pause()) {
            StateManager.get().setPaused(!StateManager.get().isPaused());
        }

        if (!StateManager.get().isPaused()) {
            _preUpdateState = StateManager.get().current();
            StateManager.get().asyncUpdate();
            StateManager.get().update();
            ParticleEngine.get().update();
            ParticleWrapper.get().update();
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
            ParticleWrapper.get().draw();
            UiElements.get().draw();
            TextPool.get().draw();
            DevConsole.get().draw();

            Window.get().end();

            Window.get().processDrawAPICalls();
            Window.get(true).processDelayedCalls();
        }
    }

    @Override
    public void render() {
        try {
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
}
