package sps.states;

import sps.entities.EntityManager;
import sps.particles.ParticleEngine;
import sps.text.TextPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StateManager {

    private static StateManager instance = new StateManager();

    public static StateManager get() {
        return instance;
    }

    private Stack<State> _states;
    private Map<State, StateDependentComponents> _components = new HashMap<State, StateDependentComponents>();

    private StateManager() {
        _states = new Stack<State>();
    }

    private void load(State state) {
        //$$$ Logger.info("=== Loading new state: " + state.getName());

        if (_components.containsKey(state)) {
            StateDependentComponents components = _components.get(state);
            EntityManager.set(components.entityManager);
            ParticleEngine.set(components.particleEngine);
            TextPool.set(components.textPool);
        }
        else {
            EntityManager.reset();
            ParticleEngine.reset();
            TextPool.reset();
        }
        _states.push(state);
        current().load();
    }

    public void push(State state) {
        if (_states.size() > 0) {
            _components.put(current(), new StateDependentComponents(EntityManager.get(), ParticleEngine.get(), TextPool.get()));
        }
        load(state);

    }

    public void pop() {
        if (_states.size() > 1) {
            State dying = _states.pop();
            TextPool.get().clear(dying);
            dying.unload();
        }
        load(current());
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
