package sps.states;

import game.GameConfig;
import sps.audio.MusicPlayer;
import sps.core.Logger;
import sps.entities.EntityManager;
import sps.graphics.Window;
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

    public static void clearTimes() {
        stateTimes = new HashMap<String, Long>();
    }

    public static String debug() {
        String result = "\"stateTimes\":{";
        int c = 0;
        for (String k : stateTimes.keySet()) {
            result += "\"" + k + "\":\"" + stateTimes.get(k) + "\"";
            if (c++ < stateTimes.keySet().size() - 1) {
                result += ",";
            }
        }
        result += "}";
        return result;
    }

    private Stack<State> _states;
    private Map<State, StateDependentComponents> _components;

    private StateManager() {
        _states = new Stack<State>();
        _components = new HashMap<State, StateDependentComponents>();
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

    private static long lastMil = System.currentTimeMillis();
    private static Map<String, Long> stateTimes = new HashMap<String, Long>();

    public void push(State state) {
        if (GameConfig.DevTimeStates) {
            Logger.info("Pushing: " + state.getName() + ". Time since last: " + ((System.currentTimeMillis() - lastMil)) / 1000f);
            lastMil = System.currentTimeMillis();
        }
        if (GameConfig.DevBotEnabled) {
            if (lastMil != 0) {
                if (!stateTimes.containsKey(state.getName())) {
                    stateTimes.put(state.getName(), 0L);
                }
                stateTimes.put(state.getName(), stateTimes.get(state.getName()) + (System.currentTimeMillis() - lastMil));
            }
            lastMil = System.currentTimeMillis();
        }
        Window.get().resetCamera();
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
