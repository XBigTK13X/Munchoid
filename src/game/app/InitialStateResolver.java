package game.app;

import game.stages.battle.Battle;
import game.stages.battle.MergeOutcome;
import game.app.core.CrashNotification;
import game.app.core.SettingsDetector;
import game.app.core.UserFiles;
import game.app.dev.DevConfig;
import game.stages.forceselection.ForceSelection;
import game.stages.population.PreloadPopulationOverview;
import game.stages.pregame.Intro;
import game.stages.pregame.PreloadMainMenu;
import game.app.save.Options;
import game.app.dev.test.*;
import sps.states.State;

import java.io.File;

public class InitialStateResolver {
    public static State create() {
        File crash = UserFiles.crash();
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
