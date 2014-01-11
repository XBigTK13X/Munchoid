package game.stages.tournament;

import game.stages.common.creatures.Creature;

public class Combatant {
    private String _name;
    private Creature _pet;

    public Combatant(String name) {
        _name = name;
    }

    public Creature getPet() {
        if (_pet == null) {
            _pet = new Creature();
        }
        return _pet;
    }

    public String getName() {
        return _name;
    }
}
