package game.stages.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.states.StateManager;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;


public class ControlsOptionsMenu extends OptionsState {
    public ControlsOptionsMenu(Sprite background) {
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

        final UIButton configure = new UIButton("Configure Controls") {
            @Override
            public void click() {
                StateManager.get().push(new InputConfigState());
            }
        };

        final UIButton view = new UIButton("View Current Controls") {
            @Override
            public void click() {
                StateManager.get().push(new ViewCurrentControls(_background));
            }
        };

        ButtonStyle style = new ButtonStyle(30, 30, 40, 10, 10);
        style.apply(configure, 0, 3);
        style.apply(view, 0, 1);
        style.apply(back, 0, 0);
    }
}

