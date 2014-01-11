package game.stages.arena;

import game.stages.battle.TimerGraphic;

import java.util.ArrayList;
import java.util.List;

public class ArenaPayload {
    private List<Catchable> _catchables = new ArrayList<Catchable>();
    private Floor _floor;
    private Player _player;
    private TimerGraphic _timer;

    public void cache(Player player) {
        _player = player;
    }

    public Player getPlayer() {
        return _player;
    }

    public void cache(Catchable catchable) {
        _catchables.add(catchable);
    }

    public List<Catchable> getCatchables() {
        return _catchables;
    }

    public void cache(Floor floor) {
        _floor = floor;
    }

    public Floor getFloor() {
        return _floor;
    }

    public void cache(TimerGraphic timer) {
        _timer = timer;
    }

    public TimerGraphic getTimer() {
        return _timer;
    }
}

