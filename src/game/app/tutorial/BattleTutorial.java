package game.app.tutorial;

import sps.bridge.Commands;

public class BattleTutorial extends Tutorial {
    public BattleTutorial() {
        addStep("To the right is your opponent.", 70, 30);
        addStep("Use the active forces to beat your opponent.", 20, 80);
        addStep("You can only use a force if it isn't [Disabled]", 20, 80);
        addStep("Activating a force will use energy.\nWeaker forces use less energy.", 5, 70);
        addStep("Click these or use the key shown when hovering over the force to use it.", 20, 80);
        addStep("You can unlock new forces by merging with fallen foes.");
        addStep("Every force applies a side effect when used.\nSome help you and some hurt you.");
        addStep("If you find that you don't have enough energy\nyou can pass by pressing the pass button or " + Commands.get("Pass") + ".");
    }
}