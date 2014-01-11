package game.app.dev;

import game.app.InitialStateResolver;
import game.app.core.InputWrapper;
import game.stages.pregame.PreloadMainMenu;
import game.app.save.Options;
import sps.console.DevConsole;
import sps.states.StateManager;

public class DevShortcuts {
    public static void handle() {
        if (DevConfig.ShortcutsEnabled && !DevConsole.get().isActive()) {
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug1()) {
                StateManager.reset().push(new PreloadMainMenu());
            }
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug2()) {
                Options.resetToDefaults();
            }
            if (InputWrapper.moveRight() && InputWrapper.moveLeft() && InputWrapper.debug2()) {
                StateManager.reset().push(InitialStateResolver.create());
            }
            if (InputWrapper.devConsole()) {
                DevConsole.get().toggle();
            }
        }
    }
}
