package game.tournament;

import game.arena.Player;
import game.creatures.Creature;
import game.states.Battle;
import sps.core.RNG;
import sps.states.StateManager;

public class Bracket {
    class BinaryTree {
        BinaryTree left;
        BinaryTree right;
        BinaryTree parent;
        Combatant winner;

        public BinaryTree(int depth) {
            left = new BinaryTree(this, depth - 1);
            right = new BinaryTree(this, depth - 1);
        }

        public BinaryTree(BinaryTree parent, int depth) {
            this.parent = parent;
            if (depth == 0) {
                winner = fighter();
            }
            else {
                left = new BinaryTree(this, depth - 1);
                right = new BinaryTree(this, depth - 1);
            }
        }
    }

    Player _player;

    public Bracket(Player player) {
        _player = player;
    }

    public void runNextMatch() {
        runNextMatch(_matches.left);
    }

    private void runNextMatch(BinaryTree tree) {
        if (tree.left == null && tree.right == null) {
            //TODO Assumes player is always on a left branch
            if (tree.winner.getName().equalsIgnoreCase("Player")) {
                Creature slot1 = tree.winner.getName().equalsIgnoreCase("Player") ? _player.getPet() : tree.winner.getPet();
                Creature slot2 = tree.parent.right.winner.getName().equalsIgnoreCase("Player") ? _player.getPet() : tree.parent.right.winner.getPet();
                StateManager.get().push(new Battle(slot1, slot2));
            }
            else {
                //TODO Thorough simulation of PC vs PC battle
                tree.parent.winner = RNG.coinFlip() ? tree.left.winner : tree.parent.right.winner;
                tree.parent.right = null;
                tree.parent.left = null;
            }
        }
        if (tree.left.winner != null && tree.right.winner == null) {
            runNextMatch(tree.right);
        }
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

    private BinaryTree _matches;

    public Bracket(int matches) {
        _matches = new BinaryTree(matches);
    }
}
