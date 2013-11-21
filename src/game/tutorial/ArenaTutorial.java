package game.tutorial;

import sps.bridge.Commands;

public class ArenaTutorial extends Tutorial {
    public ArenaTutorial() {
        addStep("Use " + Commands.get("MoveUp") + "," + Commands.get("MoveRight") + "," + Commands.get("MoveDown") + ", and " + Commands.get("MoveLeft") + " to move.");
        addStep("First you need to catch a creature, using " + Commands.get("Confirm"));
        addStep("Other creatures will turn blue if your creature can chomp them, or red if unable.");
        addStep("Chomp using " + Commands.get("Confirm") + " to build a stat bonus.\n This also eliminates an opponent without needing to battle it.");
    }
}
