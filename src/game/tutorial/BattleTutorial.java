package game.tutorial;

public class BattleTutorial extends Tutorial {
    public BattleTutorial() {
        addStep("To the right is your opponent.", 70, 30);
        addStep("Use the active forces to beat your opponent.", 20, 80);
        addStep("You can only use a force if it isn't [Disabled]", 20, 80);
        addStep("Using a force will use energy.\nWeaker forces use less energy.", 5, 30);
        addStep("If you beat an opponent, then you will have the chance to merge and access new forces.");
        addStep("To use a force, click the buttons or use the key shown when hovering over the force.", 20, 80);
        addStep("Every force applies a side effect when used.\nSome help you and some hurt you.");
    }
}
