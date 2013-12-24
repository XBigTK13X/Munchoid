package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.ui.UIButton;
import sps.states.StateManager;

public class GameplayOptionsMenu extends OptionsState {
    public GameplayOptionsMenu(Sprite background) {
        super(background);
    }

    @Override
    public void create() {
        final UIButton back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        back.setColRow(2, 3);

        back.layout();
    }
}
