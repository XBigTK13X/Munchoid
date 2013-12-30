package game.tournament;

import game.arena.Player;
import game.battle.Battle;
import game.core.GameConfig;
import game.creatures.Creature;
import game.creatures.Stats;
import game.forces.Force;
import sps.core.RNG;
import sps.states.StateManager;

import java.util.*;

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
        return _playerMatches == GameConfig.BattlesPerTournament;
    }

    public void beginPlayerMatch() {
        _playerMatches++;
        //TODO pull in a creature from an opposing combatant

        Creature opponent = new Creature();
        Stats stats = opponent.getStats();

        Stats petStats = _player.getPet().getStats();
        LinkedList<Integer> rawPetStats = new LinkedList<>();
        for (Force force : Force.values()) {
            rawPetStats.add(petStats.get(force));
        }
        Collections.shuffle(rawPetStats);

        for (Force force : Force.values()) {
            stats.set(force, RNG.next(GameConfig.TournamentStatRange * 2) - GameConfig.TournamentStatRange + rawPetStats.pop());
        }
        stats.enableStrongest();

        StateManager.get().push(new Battle(_player.getPet(), opponent, isLastMatch()));
    }
}
