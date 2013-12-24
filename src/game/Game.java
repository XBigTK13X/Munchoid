package game;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import game.arena.Arena;
import game.arena.ChompPlayer;
import game.battle.Battle;
import game.battle.MergeOutcome;
import game.creatures.PartFunction;
import game.forceselection.ForceSelection;
import game.population.PopulationOverview;
import game.population.PreloadPopulationOverview;
import game.pregame.MainMenu;
import game.save.Options;
import game.test.BackgroundGenerationTest;
import game.test.CatchableGenerationTest;
import game.test.MeterProgressTest;
import game.test.SkeletonTest;
import game.tutorial.ArenaTutorial;
import game.tutorial.BattleTutorial;
import game.tutorial.PopulationOverviewTutorial;
import sps.audio.MusicPlayer;
import sps.audio.SoundPlayer;
import sps.bridge.Commands;
import sps.bridge.SpriteTypes;
import sps.bridge.Sps;
import sps.color.Color;
import sps.console.DevConsole;
import sps.core.Logger;
import sps.core.RNG;
import sps.core.SpsConfig;
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

    private State _preUpdateState;
    private ExitPrompt _exitPrompt;

    @Override
    public void create() {
        RNG.seed((int) System.currentTimeMillis());
        Sps.setup(true);

        Assets.get().fontPack().setDefault("Aller/Aller_Rg.ttf", 50);
        Assets.get().fontPack().cacheFont("keys", "Keycaps Regular.ttf", 30);
        Assets.get().fontPack().cacheFont("UIButton", "neris/Neris-SemiBold.otf", 70);
        Assets.get().fontPack().cacheFont("Console", "ubuntu/UbuntuMono-R.ttf", 24);

        DevConsole.get().setFont("Console", 24);

        SoundPlayer.get().add("Click", "click.ogg");
        ChompPlayer.setup();

        MusicPlayer.get().add("Intro", "Intro.ogg");
        MusicPlayer.get().add("Anticipation", "Anticipation.ogg");
        MusicPlayer.get().add("MergeTheme", "MergeTheme.ogg");
        MusicPlayer.get().add("Quickly", "Quickly.ogg");
        MusicPlayer.get().add("BattleTheme", "BattleTheme.ogg");

        Window.setWindowBackground(Color.BLACK);
        Window.get(false).screenEngine().setStrategy(new FrameStrategy());
        Window.get(true).screenEngine().setStrategy(new FrameStrategy());
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());

        PartFunction.initJointSpecs();

        StateManager.get().addTutorial(PopulationOverview.class, new PopulationOverviewTutorial());
        StateManager.get().addTutorial(Battle.class, new BattleTutorial());
        StateManager.get().addTutorial(Arena.class, new ArenaTutorial());

        if (DevConfig.BotEnabled) {
            Options options = Options.load();
            options.GraphicsLowQuality = DevConfig.BotsLowQualityGraphics;
            options.FullScreen = false;
            options.WindowResolutionY = 100;
            options.WindowResolutionX = 100;
            options.apply();
            options.save();
        }

        StateManager.get().push(createInitialState());
        StateManager.get().setPaused(false);

        _exitPrompt = new ExitPrompt();

        ConsoleCommands.init();
    }

    private State createInitialState() {
        if (DevConfig.PopulationTest) {
            return new PreloadPopulationOverview();
        }
        if (DevConfig.MergeOutcomeTest) {
            return new MergeOutcome();
        }
        else if (DevConfig.BattleTest) {
            return new Battle();
        }
        else if (DevConfig.SkeletonTest) {
            return new SkeletonTest();
        }
        else if (DevConfig.BackgroundGenerationTest) {
            return new BackgroundGenerationTest();
        }
        else if (DevConfig.OutlineTest) {
            return new CatchableGenerationTest();
        }
        else if (DevConfig.MeterTest) {
            return new MeterProgressTest();
        }
        else if (DevConfig.ForceSelectionTest) {
            return new ForceSelection();
        }
        else {
            return new MainMenu();
        }
    }

    private boolean _firstResizeCall = true;

    @Override
    public void resize(int width, int height) {
        if (_firstResizeCall) {
            if (SpsConfig.get().displayLoggingEnabled) {
                Logger.info("Libgdx overrides the resolution set in config. Ignoring that resize call");
            }
            _firstResizeCall = false;
            return;
        }
        if (width != Window.Width || height != Window.Height) {
            Window.resize(width, height, Gdx.graphics.isFullscreen());
        }
    }

    private void handleDevShortcuts() {
        if (DevConfig.ShortcutsEnabled && !DevConsole.get().isActive()) {
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug1()) {
                StateManager.reset().push(new MainMenu());
            }
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug2()) {
                Options.resetToDefaults();
            }
            if (InputWrapper.moveRight() && InputWrapper.moveLeft() && InputWrapper.debug2()) {
                StateManager.reset().push(createInitialState());
            }
            if (InputWrapper.fullScreen()) {
                Options options = Options.load();
                Logger.info("Called 1");
                Window.resize(options.WindowResolutionX, options.WindowResolutionY, Gdx.graphics.isFullscreen());
                options.FullScreen = Gdx.graphics.isFullscreen();
                options.save();
            }
            if (InputWrapper.devConsole()) {
                DevConsole.get().toggle();
            }
        }
    }

    private boolean _firstRunOptionsApplied = false;

    private void update() {
        //If we do this in create(), then some platforms do not enter fullscreen properly.
        if (!_firstRunOptionsApplied) {
            Options options = Options.load();
            options.apply();
            _firstRunOptionsApplied = true;
        }

        if (_preUpdateState != StateManager.get().current()) {
            _exitPrompt = new ExitPrompt();
        }
        Input.get().update();

        if (_exitPrompt.isActive()) {
            UiElements.get().update();
            _exitPrompt.update();
        }

        handleDevShortcuts();

        if (InputWrapper.pause() && !DevConsole.get().isActive()) {
            StateManager.get().setPaused(!StateManager.get().isPaused());
        }

        DevConsole.get().update();

        if (Input.get().isActive(Commands.get("Help"))) {
            StateManager.get().showTutorial(true);
        }

        if (Input.get().isActive(Commands.get("Exit"))) {
            if (_exitPrompt.isActive()) {
                _exitPrompt.setActive(false);
            }
            else {
                if (!StateManager.get().closeTutorial()) {
                    _exitPrompt.setActive(true);
                }
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
                DevConsole.get().add("" + Gdx.graphics.getFramesPerSecond() + ": " + Gdx.graphics.getDeltaTime());
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
        SoundPlayer.get().dispose();
        MusicPlayer.get().dispose();
    }
}
