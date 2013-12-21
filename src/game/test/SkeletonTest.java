package game.test;

import game.InputWrapper;
import game.creatures.BodyPart;
import game.creatures.Creature;
import game.forces.Force;
import sps.bridge.Commands;
import sps.color.Color;
import sps.core.Logger;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

public class SkeletonTest implements State {

    private Creature _creature;
    private Text _display;
    private int _rotTarget = 1;
    private boolean _forceMode = false;
    private int _forceMagnitude = 0;

    @Override
    public void create() {
        _creature = new Creature();
        _creature.getBody().setScale(1f);
        _creature.setLocation(Screen.pos(50, 50));

        _display = TextPool.get().write("", Screen.pos(5, 90));
        _display.setFont("default", 35);
        BodyPart target = getTarget();
        target.setTint(Color.GRAY);
    }

    @Override
    public void draw() {
        _creature.draw();

    }

    private BodyPart getTarget() {
        return _creature.getBody().getParts().getAll().get(_rotTarget);
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            StateManager.get().push(new SkeletonTest());
        }
        float diff = .01f;
        if (_forceMode) {
            if (Input.get().isActive(Commands.get("MoveDown"), 0, true)) {
                Input.get().lock(Commands.get("MoveDown"), 0);
                _forceMagnitude = _forceMagnitude - 1;
                if (_forceMagnitude < 0) {
                    _forceMagnitude = 0;
                }
            }
            if (Input.get().isActive(Commands.get("MoveUp"), 0, true)) {
                Input.get().lock(Commands.get("MoveUp"), 0);
                _forceMagnitude++;
            }
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command))) {
                    Force.create(force, _forceMagnitude).apply(getTarget());
                }
            }
        }
        else {
            if (InputWrapper.moveUp()) {
                _creature.getBody().setScale(_creature.getBody().getScale() + diff);
            }
            if (InputWrapper.moveDown()) {
                _creature.getBody().setScale(_creature.getBody().getScale() - diff);
            }
            if (Input.get().isActive(Commands.get("Force1"))) {
                _creature.getBody().flipX(!_creature.getBody().isFlipX());
            }
            if (Input.get().isActive(Commands.get("Force2"))) {
                Window.get().screenEngine().getCamera().zoom += .1f;
                Window.get().screenEngine().moveCamera(-(int) Screen.width(10), -(int) Screen.height(10));
            }
            if (Input.get().isActive(Commands.get("Force3"))) {
                _creature.getBody().setScale(1f);
                _creature.getBody().reset();
                Window.get().screenEngine().getCamera().zoom = 1f;
                Window.get().screenEngine().resetCamera();
            }
            if (Input.get().isActive(Commands.get("Force4"))) {
                BodyPart target = getTarget();
                target.setTint(Color.WHITE);
                _rotTarget++;
                if (_rotTarget >= _creature.getBody().getParts().getAll().size()) {
                    _rotTarget = 1;
                }
                BodyPart newTarget = getTarget();
                newTarget.setTint(Color.GRAY);
            }
            if (Input.get().isActive(Commands.get("Force5"), 0, false)) {
                BodyPart target = getTarget();
                target.setRotation(target.getRotationDegrees() + 5);
            }
            if (Input.get().isActive(Commands.get("Force6"))) {
                BodyPart target = getTarget();
                target.setRotation(0);
                Logger.info("Pivot point:" + target.getPivot() + ", " + target.getWidth() + " x " + target.getHeight() + ", " + target.getParentConnection().GridLoc);
            }
        }

        if (InputWrapper.debug1()) {
            _forceMode = !_forceMode;
        }

        _creature.update();
        String display = "Scale: " + String.format("%.2f", _creature.getBody().getScale()) + "\n";
        display += "Window: " + Window.Width + " x " + Window.Height + "\n";
        display += "Part Size: " + getTarget().getWidth() + " x " + getTarget().getHeight() + "\n";
        Point2 rotD = getTarget().calculateRotatedDimensions();
        display += "Rot  Size: " + String.format("%.0f", rotD.X) + " x " + String.format("%.0f", rotD.Y) + "\n";
        display += "Mag: " + _forceMagnitude + "\n";
        display += "\n" + Commands.get("Confirm") + " new creature";


        if (_forceMode) {
            display += "\n" + Commands.get("Debug1") + " rotate mode" + "\n";

            display += "\n" + Commands.get("MoveUp") + " mag up";
            display += "\n" + Commands.get("MoveDown") + " mag down";
            for (Force force : Force.values()) {
                display += "\n" + Commands.get(force.Command) + " " + force.name();
            }

        }
        else {
            display += "\n" + Commands.get("Debug1") + " force mode" + "\n";

            display += "\n" + Commands.get("MoveUp") + " scale up";
            display += "\n" + Commands.get("MoveDown") + " scale down";
            display += "\n" + Commands.get("Force1") + " flipX";
            display += "\n" + Commands.get("Force2") + " zoom out";
            display += "\n" + Commands.get("Force3") + " reset";
            display += "\n" + Commands.get("Force4") + " select p";
            display += "\n" + Commands.get("Force5") + " pRotation++";
            display += "\n" + Commands.get("Force6") + " pRotation=0";
        }
        _display.setMessage(display);
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

