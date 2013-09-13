package game.pregame;

import game.ui.UIButton;
import sps.states.State;
import sps.states.StateManager;

public class GameplayOptionsMenu implements State {
    UIButton _back;

    @Override
    public void create() {
        _back = new UIButton("Back") {
            @Override
            public void click() {
                StateManager.get().pop();
            }
        };

        _back.setColRow(2, 3);
    }

    @Override
    public void draw() {
        _back.draw();
    }

    @Override
    public void update() {
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
