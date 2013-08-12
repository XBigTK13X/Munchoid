package game.tournament;

import game.GameConfig;
import game.arena.Player;
import game.creatures.Creature;
import game.states.Battle;
import sps.core.RNG;
import sps.states.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Matches {
    private List<Combatant> _fighters = new ArrayList<Combatant>();
    private boolean first = true;
    final Player _player;
    int _playerMatches = 0;


    public Matches(Player player, int bouts) {
        _player = player;
        while (--bouts > 0 || _fighters.size() == 0) {
            _fighters.add(fighter());
            _fighters.add(fighter());
        }
    }

    private Combatant fighter() {
        /*TODO Creating the creature when you create a combatant
            There used to be memory issues preventing
            more than 1 creature being loaded at a time.
            That is why we create a new creature for each match.
            This has since been solved.
        */
        if (first) {
            first = false;
            return new Combatant("Player");
        }
        return new Combatant(UUID.randomUUID().toString());
    }

    public void removeLastOpponent() {
        if (_fighters.size() > 1) {
            _fighters.remove(1);
        }
    }

    public void simulateCpuOnlyRounds() {
        for (int index = 2; index + 1 < _fighters.size(); index += 2) {
            //TODO
            //Simulate a battle between the two CPU combatants
            //Instead of picking via coin flip
            _fighters.remove(RNG.coinFlip() ? index - 2 : index - 1);
        }
    }

    private boolean isLastMatch() {
        return _playerMatches == GameConfig.TournamentMatches;
    }

    public void beginPlayerMatch() {
        _playerMatches++;
        //TODO pull in a creature from an opposing combatant
        StateManager.get().push(new Battle(_player.getPet(), new Creature(), isLastMatch()));
    }
}
