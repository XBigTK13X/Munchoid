package game.tournament;

import game.arena.Player;
import game.creatures.Creature;
import game.states.Battle;
import game.states.TournamentWin;
import sps.core.RNG;
import sps.states.StateManager;

import java.util.ArrayList;
import java.util.List;

public class Bracket {

    final Player _player;

    public void removeLastOpponent() {
        _matches._fighters.set(1, null);
    }

    class Matchups {
        private List<Combatant> _fighters = new ArrayList<Combatant>();
        private int _index;

        public Matchups(int bouts) {
            while (--bouts > 0) {
                _fighters.add(fighter());
                _fighters.add(fighter());
            }
        }

        public void next() {
            _index += 2;
            if (_fighters.size() == 1 || (_fighters.size() == 2 && _fighters.get(1) == null)) {
                StateManager.get().push(new TournamentWin());
            }
            if (_index - 1 >= _fighters.size()) {
                List<Combatant> condensed = new ArrayList<Combatant>();
                for (int ii = 0; ii < _fighters.size(); ii++) {
                    if (_fighters.get(ii) != null) {
                        condensed.add(_fighters.get(ii));
                    }
                }
                _fighters = condensed;
                _index = 2;
            }

            if (_fighters.get(_index - 2).getName().equalsIgnoreCase("Player")) {
                StateManager.get().push(new Battle(_player.getPet(), new Creature(true)));
            }
            else {
                _fighters.set(RNG.coinFlip() ? _index - 2 : _index - 1, null);
            }
        }
    }

    public void runNextMatch() {
        _matches.next();
    }

    private boolean first = true;

    private Combatant fighter() {
        //TODO Creating the creature when you create a combatant
        // Leads to memory issues on the sixth creature. There's gotta be a better way
        // to pack Atom into a smaller space
        if (first) {
            first = false;
            return new Combatant("Player");
        }
        return new Combatant(RNG.next(0, Integer.MAX_VALUE) + "");
    }

    private Matchups _matches;

    public Bracket(Player player, int matches) {
        _player = player;
        _matches = new Matchups(matches);
    }
}
