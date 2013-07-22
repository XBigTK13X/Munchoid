package game;

import sps.core.Logger;
import sps.states.StateManager;

public class MetaData {
    public static void printWin() {
        print("\"endResult\":\"WIN\"");
    }

    public static void printLose() {
        print("\"endResult\":\"LOSE\"");
    }

    private static void print(String end) {
        if (GameConfig.OptCollectMetaData) {
            String metaData = "{";

            metaData += end + ",";
            metaData += Score.get().debug() + ",";
            metaData += Score.get().petStats().debug() + ",";
            metaData += StateManager.debug() + ",";
            metaData += GameConfig.debug() + "}";

            Logger.info(metaData);
        }
    }
}
