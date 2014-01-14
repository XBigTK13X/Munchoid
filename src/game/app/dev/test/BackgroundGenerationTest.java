package game.app.dev.test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.BackgroundMaker;
import game.app.config.GameConfig;
import game.app.core.InputWrapper;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.color.Colors;
import sps.core.Logger;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.io.Input;
import sps.states.State;

public class BackgroundGenerationTest implements State {
    private static enum TestMode {
        Complex,
        Simple
    }

    private Sprite _bg;
    private Color _bgColor;
    private TestMode testMode = TestMode.Complex;

    @Override
    public void create() {
        regenBackground();
        _bgColor = Colors.randomPleasant();
    }

    @Override
    public void draw() {
        Window.get().schedule(_bg, DrawDepths.get("GameBackground"));
    }

    private void regenBackground() {
        if (testMode == TestMode.Complex) {
            if (GameConfig.OptPerformanceGraphicsSettings) {
                _bg = BackgroundMaker.radialDark();
                Logger.info("New radial");
            }
            else {
                _bg = BackgroundMaker.printedCircuitBoard();
                Logger.info("New PCB");
            }
            _bg.setPosition(0, 0);
        }
        else {
            Logger.info("New simple: " + _bgColor.toString());
            _bg = SpriteMaker.fromColors(ProcTextures.monotone(500, 500, _bgColor));
            _bg.setPosition(200, 200);
        }
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Force2"))) {
            if (testMode == TestMode.Complex) {
                Logger.info("Entering simple test mode: " + _bgColor.toString());
                testMode = TestMode.Simple;
            }
            else if (testMode == TestMode.Simple) {
                Logger.info("Entering complex test mode");
                testMode = TestMode.Complex;
            }
            regenBackground();
        }

        if (testMode == TestMode.Complex) {
            if (InputWrapper.confirm()) {
                regenBackground();
            }
            if (Input.get().isActive(Commands.get("Force1"))) {
                GameConfig.setGraphicsMode(!GameConfig.OptPerformanceGraphicsSettings);
                Logger.info("Perf: " + GameConfig.OptPerformanceGraphicsSettings);
                regenBackground();
            }
        }

        int shadePower = 1;
        if (testMode == TestMode.Simple) {
            if (Input.get().isActive(Commands.get("MoveRight"))) {
                _bgColor = Colors.brightnessShift(_bgColor, shadePower);
                regenBackground();
            }
            if (Input.get().isActive(Commands.get("MoveLeft"))) {
                _bgColor = Colors.brightnessShift(_bgColor, -shadePower);
                regenBackground();
            }
            if (Input.get().isActive(Commands.get("Confirm"))) {
                _bgColor = Colors.randomPleasant();
                regenBackground();
            }
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
        return null;
    }

    @Override
    public void pause() {
    }
}
