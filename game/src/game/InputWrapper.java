package game;

import sps.bridge.Commands;
import sps.io.Input;

public class InputWrapper {
    public static boolean devConsole() {
        return isDown("ToggleDevConsole");
    }

    public static boolean fullScreen() {
        return isDown("ToggleFullScreen");
    }

    public static boolean pop() {
        return isDown("Pop");
    }

    public static boolean moveUp() {
        return isDown("MoveUp");
    }

    public static boolean moveDown() {
        return isDown("MoveDown");
    }

    public static boolean moveRight() {
        return isDown("MoveRight");
    }

    public static boolean moveLeft() {
        return isDown("MoveLeft");
    }

    public static boolean confirm() {
        return isDown("Confirm");
    }

    public static boolean pause() {
        return isDown("Pause");
    }

    public static boolean push() {
        return isDown("Push");
    }

    public static boolean pass() {
        return isDown("Pass");
    }

    private static boolean isDown(String command) {
        return Input.get().isActive(Commands.get(command));
    }
}
