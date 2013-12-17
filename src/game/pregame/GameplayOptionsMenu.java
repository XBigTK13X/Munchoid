package game.pregame;

import game.ui.UIButton;
import sps.states.StateManager;

public class GameplayOptionsMenu extends OptionsState {
    @Override
    public void create() {
        final UIButton back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        final UIButton forw = new UIButton("Yup") {

            @Override
            public void click() {

            }
        };

        back.setColRow(2, 3);
        forw.setColRow(1, 1);
    }
}
