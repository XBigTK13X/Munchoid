package game.app.tutorial;

import game.app.config.GameConfig;

public class PopulationOverviewTutorial extends Tutorial {
    public PopulationOverviewTutorial() {
        addStep("This is the overview of your region.");
        addStep("The map shows the settlements your people have established.", 25, 50);
        addStep("You can also see how much progress\nhas been made eradicating causes of death.", 80, 85);
        addStep("Use this play by play screen to judge your progress after a tournament.", 80, 50);
        addStep("There is enough time for " + GameConfig.NumberOfTournaments + " tournaments.");
        addStep("After that the epidemic will hit.");
        addStep("Your efforts determine your region's fate.");
    }
}
