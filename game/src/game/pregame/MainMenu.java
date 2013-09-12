package game.pregame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.InputWrapper;
import game.Score;
import game.battle.Battle;
import game.population.PopulationOverview;
import game.ui.UIButton;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.Buttons;

public class MainMenu implements State {
    UIButton _start;
    UIButton _options;
    UIButton _exit;

    @Override
    public void create() {
        TextPool.get().write("Munchoid", Screen.pos(20, 90));
        StateManager.clearTimes();
        Score.reset();

        int baseCol = 1;
        int baseRow = 1;

        _start = new UIButton("Start", UIButton.x(baseCol), UIButton.y(baseRow), Commands.get("Confirm")) {
            @Override
            public void click() {
                StateManager.get().push(new PopulationOverview());
            }
        };

        _options = new UIButton("Options", UIButton.x(baseCol+1), UIButton.y(baseRow), Commands.get("Push")) {
            @Override
            public void click() {
                StateManager.get().push(new OptionsMenu());
            }
        };

        _exit = new UIButton("Exit", UIButton.x(baseCol+2), UIButton.y(baseRow)) {
            @Override
            public void click() {
                Gdx.app.exit();
            }
        };
    }

    @Override
    public void draw() {
        _start.draw();
        _options.draw();
        _exit.draw();
    }

    @Override
    public void update() {
        if (InputWrapper.confirm() || GameConfig.DevEndToEndStateLoadTest || GameConfig.DevBotEnabled) {
            _start.click();
        }
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
        return "Main Menu";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
