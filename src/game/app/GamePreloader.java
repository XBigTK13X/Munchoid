package game.app;

import game.app.config.GameConfig;
import game.app.core.UserFiles;
import game.app.dev.ConsoleCommands;
import game.app.dev.DevConfig;
import game.app.save.Options;
import game.app.tutorial.*;
import game.stages.arena.Arena;
import game.stages.battle.Battle;
import game.stages.battle.MergeOutcome;
import game.stages.population.PopulationOverview;
import sps.audio.MusicPlayer;
import sps.audio.RandomSoundPlayer;
import sps.bridge.SpriteTypes;
import sps.color.Color;
import sps.console.DevConsole;
import sps.display.Assets;
import sps.display.SpriteSheetManager;
import sps.display.Window;
import sps.display.render.FrameStrategy;
import sps.io.DefaultStateProvider;
import sps.io.Input;
import sps.io.InputBindings;
import sps.preload.PreloadChain;
import sps.preload.PreloadChainLink;
import sps.states.StateManager;
import sps.ui.MultiText;
import sps.util.GameMonitor;

public class GamePreloader {
    public static PreloadChain createPreloadChain() {
        PreloadChain preload = new PreloadChain(false) {
            @Override
            public void finish() {

            }
        };

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                InputBindings.init(UserFiles.input());
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                Assets.get().fontPack().setDefault("Aller/Aller_Rg.ttf", 50);
                Assets.get().fontPack().cacheFont("keys", "Keycaps Regular.ttf", 30);
                Assets.get().fontPack().cacheFont("UIButton", "neris/Neris-SemiBold.otf", 70);
                Assets.get().fontPack().cacheFont("Console", "ubuntu/UbuntuMono-R.ttf", 24);
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                MultiText.setDefaultFont("Console", 24);
                DevConsole.get().setFont("Console", 24);
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                RandomSoundPlayer.setup("chomp");
                RandomSoundPlayer.setup("click");
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                MusicPlayer.get().add("Intro", "keyboard.ogg");
                MusicPlayer.get().add("Anticipation", "Anticipation.ogg");
                MusicPlayer.get().add("MergeTheme", "MergeTheme.ogg");
                MusicPlayer.get().add("Quickly", "Quickly.ogg");
                MusicPlayer.get().add("BattleTheme", "BattleTheme.ogg");
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                Window.setWindowBackground(Color.BLACK);
                Window.get(false).screenEngine().setStrategy(new FrameStrategy());
                Window.get(true).screenEngine().setStrategy(new FrameStrategy());
                Input.get().setup(new DefaultStateProvider());
                SpriteSheetManager.setup(SpriteTypes.getDefs());
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                Window.setWindowBackground(Color.BLACK);
                Window.get(false).screenEngine().setStrategy(new FrameStrategy());
                Window.get(true).screenEngine().setStrategy(new FrameStrategy());
                Input.get().setup(new DefaultStateProvider());
                SpriteSheetManager.setup(SpriteTypes.getDefs());
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                Window.setWindowBackground(Color.BLACK);
                Window.get(false).screenEngine().setStrategy(new FrameStrategy());
                Window.get(true).screenEngine().setStrategy(new FrameStrategy());
                Input.get().setup(new DefaultStateProvider());
                SpriteSheetManager.setup(SpriteTypes.getDefs());
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                Tutorials.get().add(PopulationOverview.class, new PopulationOverviewTutorial());
                Tutorials.get().add(Battle.class, new BattleTutorial());
                Tutorials.get().add(Arena.class, new ArenaTutorial());
                Tutorials.get().add(MergeOutcome.class, new MergeTutorial());
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                if (DevConfig.BotEnabled) {
                    Options.get().GraphicsLowQuality = DevConfig.BotsLowQualityGraphics;
                    Options.get().FullScreen = false;
                    Options.get().WindowResolutionY = 100;
                    Options.get().WindowResolutionX = 100;
                    Options.get().apply();
                    Options.get().save();
                }
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                Options.load();
                Options.get().apply();
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                StateManager.get().push(InitialStateResolver.create());
            }
        });

        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                ConsoleCommands.init();
            }
        });

        final String error = "Unfortunately, an error caused Munchoid to freeze.\nThis likely happened because the graphics settings were too high for this computer.\nYou might be able to play by changing the graphics from Pretty to Fast in the Options menu.";
        preload.add(new PreloadChainLink() {
            @Override
            public void process() {
                GameMonitor.monitor(UserFiles.crash(), error, GameConfig.ThreadMaxStalledMilliseconds);
            }
        });
        return preload;
    }
}
