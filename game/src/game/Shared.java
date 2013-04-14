package game;

import game.creatures.Creature;

public class Shared {
    private static Shared __instance;

    public static Shared get() {
        if (__instance == null) {
            __instance = new Shared();
        }
        return __instance;
    }

    private Shared() {

    }

    private Creature _playerCreature;

    public Creature playerCreature() {
        return _playerCreature;
    }

    public void setPlayerCreature(Creature creature) {
        _playerCreature = creature;
    }
}
