package game.core;

import game.battle.Battle;
import game.battle.MergeOutcome;
import game.dev.DevConfig;
import game.forceselection.ForceSelection;
import game.population.PreloadPopulationOverview;
import game.pregame.Intro;
import game.pregame.PreloadMainMenu;
import game.save.Options;
import game.test.*;
import sps.core.Loader;
import sps.states.State;

import java.io.File;

public class InitialStateResolver {
    public static State create() {
        File crash = Loader.get().userSave("Munchoid", "game.crash");
        if (crash.exists()) {
            return new CrashNotification(crash);
        }
        else if(!Options.load().SettingsDetected){
            return new SettingsDetector();
        }
        else if (DevConfig.PopulationTest) {
            return new PreloadPopulationOverview();
        }
        else if (DevConfig.MergeOutcomeTest) {
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
        else if (DevConfig.SilhouetteTest) {
            return new SilhouetteTest();
        }
        else if (DevConfig.EndGameTest) {
            return new EndGameTest();
        }
        else {
            if (Options.load().ShowIntro) {
                return new Intro(true);
            }
            else {
                return new PreloadMainMenu();
            }
        }
    }
}
