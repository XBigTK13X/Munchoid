package game.test;

import game.InputWrapper;
import game.creatures.Creature;
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

    @Override
    public void create() {
        _creature = new Creature();
        _creature.getBody().setScale(1f);
        _creature.setLocation(Screen.pos(50, 50));

        _scale = TextPool.get().write("", Screen.pos(5, 90));
    }

    @Override
    public void draw() {
        _creature.draw();

    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            Logger.info("What");
            StateManager.get().push(new SkeletonTest());
        }
        float diff = .01f;
        if (InputWrapper.moveUp()) {
            _creature.getBody().setScale(_creature.getBody().getScale() + diff);
        }
        if (InputWrapper.moveDown()) {
            _creature.getBody().setScale(_creature.getBody().getScale() - diff);
        }
        if (Input.get().isActive(Commands.get("Force1"))) {
            _creature.getBody().setScale(1f);
        }
        if (Input.get().isActive(Commands.get("Force6"))) {
            _creature.getBody().flipX(!_creature.getBody().isFlipX());
        }
        if (Input.get().isActive(Commands.get("Force2"))) {
            Window.get().getCamera().zoom += .1f;
            Window.get().moveCamera(-(int) Screen.width(10), -(int) Screen.height(10));
        }

        _creature.update();
        String display = "Scale: " + _creature.getBody().getScale() + "\n";
        display += "\n" + Commands.get("Force6") + " to flipX";
        display += "\n" + Commands.get("Confirm") + " new creature";
        display += "\n" + Commands.get("MoveUp") + " scale up";
        display += "\n" + Commands.get("MoveDown") + " scale down";
        display += "\n" + Commands.get("Force1") + " scale reset";
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
