package sps.ui;

import sps.core.Logger;
import sps.entities.HitTest;
import sps.io.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buttons {
    public interface User {
        Bounds getBounds();

        void normal();

        void over();

        void onClick();
    }

    private enum State {
        Outside,
        Over,
        Clicked
    }

    private static Buttons __instance;


    public static Buttons get() {
        if (__instance == null) {
            __instance = new Buttons();
        }
        return __instance;
    }

    public static void reset() {
        __instance = new Buttons();
    }

    private List<User> _users;
    private Map<User, State> _states;

    private Buttons() {
        _states = new HashMap<User, State>();
        _users = new ArrayList<User>();
    }

    public void add(User user) {
        _users.add(user);
        _states.put(user, null);
    }

    public void update() {
        for (User user : _users) {
            boolean mouseOver = HitTest.inBox(Input.get().x(), Input.get().y(), user.getBounds());
            boolean mouseDown = Input.get().isMouseDown();
            if (!mouseOver || _states.get(user) == State.Clicked) {
                _states.put(user, State.Outside);
                user.normal();
            }
            if (mouseOver && !mouseDown) {
                _states.put(user, State.Over);
                user.over();
            }
            if (_states.get(user) == State.Over && mouseOver && mouseDown) {
                Logger.info("CLICK");
                _states.put(user, State.Clicked);
                user.onClick();
            }
        }
    }
}
