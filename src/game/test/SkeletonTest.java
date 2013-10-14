package game.test;

import com.badlogic.gdx.graphics.Color;
import game.InputWrapper;
import game.creatures.BodyPart;
import game.creatures.Creature;
import game.forces.Force;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.display.Screen;
import sps.display.Window;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

public class SkeletonTest implements State {

    private Creature _creature;
    private Text _scale;
    private int _rotTarget = 1;
    private boolean _forceMode = false;
    private int _forceMagnitude = 10;

    @Override
    public void create() {
        _creature = new Creature();
        _creature.getBody().setScale(1f);
        _creature.setLocation(Screen.pos(50, 50));

        _scale = TextPool.get().write("", Screen.pos(5, 90));
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
        if (InputWrapper.moveUp()) {
            _creature.getBody().setScale(_creature.getBody().getScale() + diff);
        }
        if (InputWrapper.moveDown()) {
            _creature.getBody().setScale(_creature.getBody().getScale() - diff);
        }
        if (_forceMode) {
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command))) {
                    Force.create(force, _forceMagnitude).apply(getTarget());
                }
            }
        }
        else {
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
        String display = "Scale: " + _creature.getBody().getScale() + "\n";
        display += "Window: " + Window.Width + " x " + Window.Height + "\n";
        display += "\n" + Commands.get("Confirm") + " new creature";
        display += "\n" + Commands.get("MoveUp") + " scale up";
        display += "\n" + Commands.get("MoveDown") + " scale down";

        if (_forceMode) {
            display += "\n" + Commands.get("Debug1") + " rotate mode" + "\n";
            for (Force force : Force.values()) {
                display += "\n" + Commands.get(force.Command) + " " + force.name();
            }

        }
        else {
            display += "\n" + Commands.get("Debug1") + " force mode" + "\n";
            display += "\n" + Commands.get("Force1") + " flipX";
            display += "\n" + Commands.get("Force2") + " zoom out";
            display += "\n" + Commands.get("Force3") + " reset";
            display += "\n" + Commands.get("Force4") + " select p";
            display += "\n" + Commands.get("Force5") + " pRotation++";
            display += "\n" + Commands.get("Force6") + " pRotation=0";
        }
        _scale.setMessage(display);
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
