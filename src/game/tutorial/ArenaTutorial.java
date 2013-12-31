package game.tutorial;

import sps.bridge.Commands;

public class ArenaTutorial extends Tutorial {
    public ArenaTutorial() {
        addStep("Use " + Commands.get("MoveUp") + "," + Commands.get("MoveRight") + "," + Commands.get("MoveDown") + ", and " + Commands.get("MoveLeft") + " to move.");
        addStep("First you need to catch a creature, using " + Commands.get("Confirm"));
        addStep("A creature turns blue if your pet can chomp it.\nIt turns red if your pet cannot.");
        addStep("The arrow over your head will always point to a chompable creature.");
        addStep("You only have a few moments before each fight to chomp.");
        addStep("The timer in the upper left shows when the next battle starts.");
        addStep("Chomp using " + Commands.get("Confirm") + " to build a stat bonus.\nThis eliminates an opponent without battling it.");
    }
}
