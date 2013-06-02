package game.states;

import game.creatures.Body;
import game.skeleton.Skeleton;
import sps.bridge.Commands;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;

public class AnimationTest implements State {

    private Skeleton skeleton;
    private Body body;

    @Override
    public void create() {
        int jointCount = 4;
        skeleton = new Skeleton(jointCount);
        body = new Body(jointCount);

    }

    @Override
    public void draw() {
        skeleton.draw();
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            StateManager.get().push(new AnimationTest());
        }
        skeleton.update();
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
