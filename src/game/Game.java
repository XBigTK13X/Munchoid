package game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import game.battle.Battle;
import game.battle.MergeOutcome;
import game.creatures.PartFunction;
import game.population.PopulationOverview;
import game.population.PreloadPopulationOverview;
import game.pregame.MainMenu;
import game.save.Options;
import game.test.BackgroundGenerationTest;
import game.test.SkeletonTest;
import game.tutorial.BattleTutorial;
import game.tutorial.PopulationOverviewTutorial;
import sps.bridge.Commands;
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
import sps.particles.ParticleWrapper;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.UiElements;

public class Game implements ApplicationListener {
    @Override
    public void create() {
        RNG.seed((int) System.currentTimeMillis());
        Sps.setup(true);

        Assets.get().fontPack().setDefault("Aller/Aller_Rg.ttf", 50);
        Assets.get().fontPack().cacheFont("keys", "Keycaps Regular.ttf", 30);
        Assets.get().fontPack().cacheFont("UIButton", "neris/Neris-SemiBold.otf", 70);

        Window.setWindowBackground(Color.BLACK);
        Window.get(false).screenEngine().setStrategy(new FrameStrategy());
        Window.get(true).screenEngine().setStrategy(new FrameStrategy());
        Window.setRefreshInstance(this);
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());

        PartFunction.initJointSpecs();

        StateManager.get().addTutorial(PopulationOverview.class, new PopulationOverviewTutorial());
        StateManager.get().addTutorial(Battle.class, new BattleTutorial());

        StateManager.get().push(createInitialState());
        StateManager.get().setPaused(false);

        Options.load().apply();
    }

    private State createInitialState() {
        if (GameConfig.DevPopulationTest) {
            return new PreloadPopulationOverview();
        }
        if (GameConfig.DevMergeOutcomeTest) {
            return new MergeOutcome();
        }
        else if (GameConfig.DevBattleTest) {
            return new Battle();
        }
        else if (GameConfig.DevSkeletonTest) {
            return new SkeletonTest();
        }
        else if (GameConfig.DevBackgroundGenerationTest) {
            return new BackgroundGenerationTest();
        }
        else {
            return new MainMenu();
        }
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
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug1()) {
                StateManager.reset().push(new MainMenu());
            }
            if (InputWrapper.moveRight() && InputWrapper.moveLeft() && InputWrapper.debug2()) {
                StateManager.reset().push(createInitialState());
            }
            if (InputWrapper.fullScreen()) {
                Options options = Options.load();
                Window.toggleFullScreen(!options.FullScreen, options.WindowResolutionX, options.WindowResolutionY);
                options.FullScreen = !options.FullScreen;
                options.save();
            }
            if (InputWrapper.devConsole()) {
                DevConsole.get().toggle();
            }
        }

        if (InputWrapper.pause()) {
            StateManager.get().setPaused(!StateManager.get().isPaused());
        }

        if (Input.get().isActive(Commands.get("Help"))) {
            StateManager.get().showTutorial(true);
        }

        if (Input.get().isActive(Commands.get("Exit"))) {
            if (!StateManager.get().closeTutorial()) {
                //TODO Prompt for game exit
            }
        }

        if (!StateManager.get().isPaused()) {
            _preUpdateState = StateManager.get().current();
            StateManager.get().asyncUpdate();
            StateManager.get().update();
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

            StateManager.get().draw();
            UiElements.get().draw();
            TextPool.get().draw();
            DevConsole.get().draw();
            ParticleWrapper.get().draw();

            Window.processDrawCalls();
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
