package game.arena;

import java.util.ArrayList;
import java.util.List;

public class ArenaPieces {
    private List<Catchable> _catchables = new ArrayList<Catchable>();
    private Floor _floor;
    private Player _player;

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
}

