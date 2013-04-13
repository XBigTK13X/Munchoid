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

    private void loadCurrent() {
        //$$$ Logger.info("=== Loading new state: " + state.getName());

        if (_components.containsKey(current())) {
            StateDependentComponents components = _components.get(current());
            EntityManager.set(components.entityManager);
            ParticleEngine.set(components.particleEngine);
            TextPool.set(components.textPool);
        }
        else {
            EntityManager.reset();
            ParticleEngine.reset();
            TextPool.reset();
        }

        current().load();
    }

    public void push(State state) {
        if (_states.size() > 0) {
            _components.put(current(), new StateDependentComponents(EntityManager.get(), ParticleEngine.get(), TextPool.get()));
        }
        _states.push(state);
        loadCurrent();
    }

    public void pop() {
        if (_states.size() > 1) {
            State dying = _states.pop();
            TextPool.get().clear(dying);
            dying.unload();
            loadCurrent();
        }
    }

    public void draw() {
        current().draw();
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
