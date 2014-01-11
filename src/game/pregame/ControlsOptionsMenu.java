package game.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.states.StateManager;
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

        final UIButton view = new UIButton("View") {
            @Override
            public void click() {
                //TODO StateManager.get().push(new ViwConfigState());
            }
        };

        configure.setColRow(3, 1);
        view.setColRow(1, 1);
        back.setColRow(2, 3);

        configure.layout();
        view.layout();
        back.layout();
    }
}

