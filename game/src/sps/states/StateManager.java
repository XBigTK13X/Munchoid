package sps.states;

import sps.audio.MusicPlayer;
import sps.entities.EntityManager;
import sps.particles.ParticleEngine;
import sps.text.TextPool;
import sps.ui.UiElements;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StateManager {

    private static StateManager instance = new StateManager();

    public static StateManager get() {
        return instance;
    }

    public static StateManager reset() {
        if (instance != null) {
            instance.removeAll();
        }
        instance = new StateManager();
        return get();
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
            MusicPlayer.set(components.musicPlayer);
        }
        else {
            EntityManager.reset();
            ParticleEngine.reset();
            TextPool.reset();
            MusicPlayer.reset();
        }
        //TODO Make UiElements a SDC
        UiElements.reset();
        current().load();
    }

    public void push(State state) {
        boolean isNewState = false;
        if (_states.size() > 0) {
            _components.put(current(), new StateDependentComponents(EntityManager.get(), ParticleEngine.get(), TextPool.get(), MusicPlayer.get()));
        }
        if (!_states.contains(state)) {
            isNewState = true;
        }
        _states.push(state);
        loadCurrent();
        if (isNewState) {
            state.create();
        }
    }

    public void removeAll() {
        while (_states.size() > 0) {
            pop(true);
        }
    }

    public void pop() {
        pop(false);
    }

    public void pop(boolean force) {
        if (_states.size() > 1 || (force && _states.size() > 0)) {
            State dying = _states.pop();
            TextPool.get().clear(dying);
            dying.unload();
            if (_states.size() > 0 && !force) {
                loadCurrent();
            }
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
