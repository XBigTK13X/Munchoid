package sps.ui;

public class UiElements {
    public static UiElements __instance;

    public static UiElements get() {
        if (__instance == null) {
            __instance = new UiElements();
        }
        return __instance;
    }

    public static void reset() {
        ToolTip.reset();
        Buttons.reset();
    }

    private UiElements() {

    }

    public void update() {
        ToolTip.get().update();
        Buttons.get().update();
    }

    public void draw() {
        ToolTip.get().draw();
    }
}
