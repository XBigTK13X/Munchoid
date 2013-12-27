package game.dev;

import com.badlogic.gdx.Gdx;
import game.InitialStateResolver;
import game.InputWrapper;
import game.pregame.MainMenu;
import game.save.Options;
import sps.console.DevConsole;
import sps.states.StateManager;

public class DevShortcuts {
    public static void handle() {
        if (DevConfig.ShortcutsEnabled && !DevConsole.get().isActive()) {
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug1()) {
                StateManager.reset().push(new MainMenu());
            }
            if (InputWrapper.moveDown() && InputWrapper.moveUp() && InputWrapper.debug2()) {
                Options.resetToDefaults();
            }
            if (InputWrapper.moveRight() && InputWrapper.moveLeft() && InputWrapper.debug2()) {
                StateManager.reset().push(InitialStateResolver.create());
            }
            if (InputWrapper.fullScreen()) {
                Options options = Options.load();
                options.FullScreen = !Gdx.graphics.isFullscreen();
                options.apply();
                options.save();
            }
            if (InputWrapper.devConsole()) {
                DevConsole.get().toggle();
            }
        }
    }
}
