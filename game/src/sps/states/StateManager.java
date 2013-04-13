package sps.states;

import sps.text.TextPool;

import java.util.Stack;

public class StateManager {

    private static StateManager instance = new StateManager();

    public static StateManager get() {
        return instance;
    }


    // TODO
    // Its currently the case that writing too much text in one state could
    // prevent a previous state from retrieving a needed text object
    // Could fix this by giving each State its own TextPool
    private Stack<State> _states;

    private StateManager() {
        _states = new Stack<State>();
    }

    public void push(State state) {
        _states.push(state);

        //$$$ Logger.info("=== Loading new state: " + state.getName());
        current().load();
    }

    public void pop() {
        if (_states.size() > 1) {
            State dying = _states.pop();
            TextPool.get().clear(dying);
            dying.unload();
        }
    }

    public void draw() {
        current().draw();
    }

    public void loadContent() {
        current().load();
    }

    public void update() {
        current().update();
    }

    public void asyncUpdate() {
        current().asyncUpdate();
    }

    public void resize(int width, int height) {
        current().resize(width, height);
    }

    public State current() {
        return _states.peek();
    }

}
