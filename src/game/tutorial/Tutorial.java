package game.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.bridge.SpriteTypes;
import sps.display.*;
import sps.draw.Colors;
import sps.draw.SpriteMaker;
import sps.io.Input;
import sps.text.Text;
import sps.text.TextPool;

import java.util.ArrayList;
import java.util.List;

public class Tutorial {
    private static Sprite __background;
    private static Sprite __arrow;

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

            SpriteInfo arrowInfo = SpriteSheetManager.getSpriteInfo(SpriteTypes.get("Arrow"));
            __arrow = Assets.get().sprite(arrowInfo.SpriteIndex);
        }
        _steps = new ArrayList<>();
    }

    public void load() {
        _display = TextPool.get().write("", Screen.pos(10, 30));
        _display.setDepth(DrawDepths.get("TutorialText"));
        _currentStepIndex = 0;
        _finished = false;
    }

    public void addStep(String message) {
        _steps.add(new Step(0, 0, message));
    }

    public void addStep(String message, int xPercent, int yPercent) {
        _steps.add(new Step((int) Screen.width(xPercent), (int) Screen.height(yPercent), message));
    }

    private void refreshDisplay() {
        _currentStep = _steps.get(_currentStepIndex);
        String message = _currentStep.getMessage();
        message += "\n\n\t\t(Press " + Commands.get("AdvanceTutorial") + " to continue";
        _display.setMessage(message);
        if (!_currentStep.getArrowLocation().isZero()) {
            __arrow.setPosition(_currentStep.getArrowLocation().X, _currentStep.getArrowLocation().Y);
            __arrow.setRotation(45);
            __arrow.setColor(Colors.randomPleasant());
        }
        else {
            __arrow.setColor(new Color(0, 0, 0, 0));
        }
    }

    public void update() {
        if (_finished) {
            return;
        }
        Window.get().schedule(__background, DrawDepths.get("TutorialBackground"));
        Window.get().schedule(__arrow, DrawDepths.get("TutorialArrow"));
        if (_steps.size() > 0 && _display.getMessage().isEmpty()) {
            refreshDisplay();
        }
        if (Input.get().isActive(Commands.get("AdvanceTutorial"))) {
            if (_currentStepIndex < _steps.size() - 1) {
                _currentStepIndex++;
                refreshDisplay();
            }
            else {
                close();
            }
        }
    }

    public void close() {
        _finished = true;
        _display.setVisible(false);
    }

    public boolean isFinished() {
        return _finished;
    }
}
