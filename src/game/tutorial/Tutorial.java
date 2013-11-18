package game.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.InputWrapper;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.io.Input;
import sps.text.Text;
import sps.text.TextPool;

import java.util.ArrayList;
import java.util.List;

public class Tutorial {
    private static Sprite __background;

    private boolean _finished;

    private List<Step> _steps;
    private Step _currentStep;
    private int _currentStepIndex;
    private Text _display;

    public Tutorial() {
        if (__background == null) {
            Color bg = new Color(Color.BLACK);
            bg.a = .8f;
            __background = SpriteMaker.get().pixel(bg);
            __background.setSize(Screen.width(100), Screen.height(100));
        }
        _steps = new ArrayList<>();
        _display = TextPool.get().write("", Screen.pos(10, 30));
        _display.setDepth(DrawDepths.get("TutorialText"));
    }

    public void addStep(String message, int x, int y) {
        _steps.add(new Step(x, y, message));
    }

    private void refreshDisplay() {
        _currentStep = _steps.get(_currentStepIndex);
        String message = _currentStep.getMessage();
        message += "\n(Press " + Commands.get("Confirm") + " and " + Commands.get("Force1") + ") to continue";
        _display.setMessage(message);
    }

    public void update() {
        if (_finished) {
            return;
        }
        Window.get().schedule(__background, DrawDepths.get("TutorialBackground"));
        if (_steps.size() > 0 && _display.getMessage().isEmpty()) {
            refreshDisplay();
        }
        if (InputWrapper.confirm() && Input.get().isActive(Commands.get("Force1"))) {
            if (_currentStepIndex < _steps.size()) {
                _currentStepIndex++;
                refreshDisplay();
            }
            else {
                _finished = true;
            }
        }
    }

    public boolean isFinished() {
        return _finished;
    }
}
