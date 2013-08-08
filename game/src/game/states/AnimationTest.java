package game.states;

import game.creatures.Creature;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

public class AnimationTest implements State {

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
        if (Input.get().isActive(Commands.get("Confirm"))) {
            StateManager.get().push(new AnimationTest());
        }
        float diff = .01f;
        if (Input.get().isActive(Commands.get("MoveUp"))) {
            _creature.getBody().setScale(_creature.getBody().getScale() + diff);
        }
        if (Input.get().isActive(Commands.get("MoveDown"))) {
            _creature.getBody().setScale(_creature.getBody().getScale() - diff);
        }
        if (Input.get().isActive(Commands.get("Force1"))) {
            _creature.getBody().setScale(1f);
        }
        if (Input.get().isActive(Commands.get("Force6"))) {
            _creature.getBody().flipX(!_creature.getBody().isFlipX());
        }

        _creature.update();
        String display = "Scale: " + _creature.getBody().getScale() + "\n";
        display += "\n[" + Commands.get("Force6").key() + "] to flipX";
        display += "\n[" + Commands.get("Confirm").key() + "] new creature";
        display += "\n[" + Commands.get("MoveUp").key() + "] scale up";
        display += "\n[" + Commands.get("MoveDown").key() + "] scale down";
        display += "\n[" + Commands.get("Force1").key() + "] scale reset";
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
