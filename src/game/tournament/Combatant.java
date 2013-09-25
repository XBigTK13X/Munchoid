package game.tournament;

import game.creatures.Creature;

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
