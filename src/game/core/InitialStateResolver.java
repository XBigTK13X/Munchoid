package game.core;

import game.battle.Battle;
import game.battle.MergeOutcome;
import game.dev.DevConfig;
import game.forceselection.ForceSelection;
import game.population.PreloadPopulationOverview;
import game.pregame.Intro;
import game.pregame.MainMenu;
import game.save.Options;
import game.test.*;
import sps.states.State;

public class InitialStateResolver {
    public static State create() {
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
        else if (DevConfig.SilhouetteTest) {
            return new SilhouetteTest();
        }
        else {
            if (Options.load().ShowIntro) {
                return new Intro(true);
            }
            else {
                return new MainMenu();
            }
        }
    }
}
