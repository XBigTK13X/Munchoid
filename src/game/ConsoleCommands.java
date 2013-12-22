package game;

import game.battle.Battle;
import game.creatures.Creature;
import game.forces.Force;
import sps.bridge.EntityTypes;
import sps.console.DevConsole;
import sps.console.DevConsoleAction;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.states.StateManager;

import java.util.List;

public class ConsoleCommands {
    private ConsoleCommands() {
    }

    public static void init() {
        DevConsole.get().register(new DevConsoleAction("PLAYERFORCE") {
            @Override
            public String act(int[] input) {
                if (input.length != 2) {
                    return "forceId [1,6] and value [0,100] required for PLAYERFORCE";
                }

                int forceId = input[0];
                int forceValue = input[1];
                Force force = Force.values()[forceId - 1];
                List<Entity> creatures = EntityManager.get().getEntities(EntityTypes.get("Creature"));
                if (creatures.isEmpty()) {
                    return "No creatures found";
                }
                for (Entity e : creatures) {
                    Creature c = (Creature) e;
                    if (c.isOwned()) {
                        c.getStats().set(force, forceValue);
                        c.getStats().setEnabled(force, true);
                        Battle battle = (Battle) StateManager.get().current();
                        battle.rebuildHud();
                        return "Player pet " + force.name() + " set to " + forceValue;
                    }
                }
                return "Player does not have a pet";

            }
        });
    }
}
