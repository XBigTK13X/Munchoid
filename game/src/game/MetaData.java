package game;

import sps.core.Logger;
import sps.states.StateManager;

public class MetaData {
    private static int version = 1;

    public static void printWin() {
        print("\"endResult\":\"WIN\"");
    }

    public static void printLose() {
        print("\"endResult\":\"LOSE\"");
    }

    private static void print(String end) {
        if (GameConfig.OptCollectMetaData) {
            String metaData = "{";
            metaData += "\"version\":\"" + version + "\"" + ",";
            metaData += end + ",";
            metaData += Score.get().json() + ",";
            metaData += Score.get().petStats().json() + ",";
            metaData += StateManager.json() + ",";
            metaData += GameConfig.json() + "}";

            Logger.info(metaData);
        }
    }
}
