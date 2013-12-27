package game.dev;

import game.GameConfig;
import game.battle.Battle;
import game.creatures.Creature;
import game.forces.Force;
import sps.bridge.EntityTypes;
import sps.console.DevConsole;
import sps.console.DevConsoleAction;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.states.StateManager;
import sps.util.Memory;

import java.util.List;

public class ConsoleCommands {
    private ConsoleCommands() {
    }

    public static void init() {
        DevConsole.get().register(new DevConsoleAction("player.setforce") {
            @Override
            public String act(int[] input) {
                if (input.length != 2) {
                    return "forceId [1,6] and value [0,100] required for player.setforce";
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

        DevConsole.get().register(new DevConsoleAction("player.setallforces") {
            @Override
            public String act(int[] input) {
                if (input.length != 1) {
                    return "value [0,100] required for player.setforce";
                }
                List<Entity> creatures = EntityManager.get().getEntities(EntityTypes.get("Creature"));
                if (creatures.isEmpty()) {
                    return "No creatures found";
                }

                int forceValue = input[0];
                for (Entity e : creatures) {
                    Creature c = (Creature) e;
                    if (c.isOwned()) {
                        for (Force force : Force.values()) {
                            c.getStats().set(force, forceValue);
                            c.getStats().setEnabled(force, true);
                        }
                        Battle battle = (Battle) StateManager.get().current();
                        battle.rebuildHud();
                        return "Player forces set to: " + forceValue;
                    }
                }
                return "Player does not have a pet";

            }
        });

        DevConsole.get().register(new DevConsoleAction("battlehud.enemystats") {
            @Override
            public String act(int[] input) {
                GameConfig.BattleEnableEnemyForcesHUD = !GameConfig.BattleEnableEnemyForcesHUD;
                if (StateManager.get().hasAny(Battle.class)) {
                    Battle battle = (Battle) StateManager.get().current();
                    battle.rebuildHud();
                }
                return "Enemy forces HUD  " + (GameConfig.BattleEnableEnemyForcesHUD ? "enabled" : "disabled");
            }
        });

        DevConsole.get().register(new DevConsoleAction("memory.debug") {
            @Override
            public String act(int[] input) {
                return Memory.getDebugInfo();
            }
        });
    }
}
