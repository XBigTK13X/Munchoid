package game.core;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import game.arena.Arena;
import game.arena.ChompPlayer;
import game.battle.Battle;
import game.creatures.PartFunction;
import game.dev.ConsoleCommands;
import game.dev.DevConfig;
import game.dev.DevShortcuts;
import game.population.PopulationOverview;
import game.save.Options;
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
import sps.ui.MultiText;
import sps.ui.UiElements;
import sps.util.CoolDown;

public class Game implements ApplicationListener {

    private State _preUpdateState;
    private boolean _firstCreateOptionsLoaded = false;
    private boolean _firstUpdateOptionsLoaded = false;
    private CoolDown _persistResizeOperation = new CoolDown(.1f);
    private boolean _firstResizeCall = true;

    @Override
    public void create() {
        RNG.naturalReseed();
        Sps.setup(true);

        Assets.get().fontPack().setDefault("Aller/Aller_Rg.ttf", 50);
        Assets.get().fontPack().cacheFont("keys", "Keycaps Regular.ttf", 30);
        Assets.get().fontPack().cacheFont("UIButton", "neris/Neris-SemiBold.otf", 70);
        Assets.get().fontPack().cacheFont("Console", "ubuntu/UbuntuMono-R.ttf", 24);

        MultiText.setDefaultFont("Console", 24);
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

        if (!_firstCreateOptionsLoaded) {
            Options options = Options.load();
            options.apply();
            _firstCreateOptionsLoaded = true;
        }

        StateManager.get().push(InitialStateResolver.create());
        StateManager.get().setPaused(false);

        ConsoleCommands.init();

        _persistResizeOperation.zeroOut();
    }

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
            _persistResizeOperation.reset();
            Window.resize(width, height, Gdx.graphics.isFullscreen());
        }
    }

    private void handleWindowQuerks() {
        if (!_persistResizeOperation.isCooled()) {
            if (_persistResizeOperation.updateAndCheck()) {
                Logger.info("Persisting");
                Options options = Options.load();
                options.WindowResolutionX = Window.Width;
                options.WindowResolutionY = Window.Height;
                options.apply();
                options.save();
                _persistResizeOperation.zeroOut();
            }
        }
        if (!_firstUpdateOptionsLoaded) {
            Options options = Options.load();
            options.apply();
            _firstUpdateOptionsLoaded = true;
        }
    }

    private void handleUserInput() {
        Input.get().update();

        if (InputWrapper.pause() && !DevConsole.get().isActive()) {
            StateManager.get().setPaused(!StateManager.get().isPaused());
        }

        if (InputWrapper.fullScreen()) {
            Options options = Options.load();
            options.FullScreen = !Gdx.graphics.isFullscreen();
            options.apply();
            options.save();
        }

        if (Input.get().isActive(Commands.get("Help"))) {
            StateManager.get().showTutorial(true);
        }

        if (Input.get().isActive(Commands.get("Exit"))) {
            if (ExitPrompt.get().isActive()) {
                ExitPrompt.get().setActive(false);
            }
            else {
                if (!StateManager.get().closeTutorial()) {
                    ExitPrompt.get().setActive(true);
                }
            }
        }
    }

    private void nonGameUpdates() {
        handleWindowQuerks();
        handleUserInput();
        ExitPrompt.get().update();
        DevShortcuts.handle();
        DevConsole.get().update();
    }

    private void update() {
        nonGameUpdates();

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
