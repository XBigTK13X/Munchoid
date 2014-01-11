package game.app.dev;

import game.app.core.ArenaScore;
import game.config.GameConfig;
import game.config.UIConfig;
import game.app.core.WorldScore;
import sps.core.Logger;
import sps.states.StateManager;
import sps.util.JSON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MetaData {
    private static int version = 1;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {
        printWin();
    }

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static void printWin() {
        print("\"endResult\":\"WIN\"");
    }

    public static void printLose() {
        print("\"endResult\":\"LOSE\"");
    }

    private static void print(String end) {
        if (GameConfig.OptCollectMetaData) {
            Date date = new Date();
            System.out.println();
            String metaData = "{";
            metaData += "\"version\":\"" + version + "\"" + ",";
            metaData += end + ",";
            metaData += ArenaScore.get().json() + ",";
            metaData += ArenaScore.get().petStats().json() + ",";
            metaData += StateManager.json() + ",";
            metaData += JSON.formatFields(GameConfig.class) + ",";
            metaData += JSON.formatFields(DevConfig.class) + ",";
            metaData += JSON.formatFields(UIConfig.class) + ",";
            metaData += JSON.formatFields(WorldScore.class) + ",";
            metaData += "\"timestamp\":\"" + dateFormat.format(date) + "\"";
            metaData += "}";

            Logger.metaData(metaData);
        }
    }
}
