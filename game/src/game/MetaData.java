package game;

import sps.core.Logger;
import sps.states.StateManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MetaData {
    private static int version = 1;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args){
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
            metaData += Score.get().json() + ",";
            metaData += Score.get().petStats().json() + ",";
            metaData += StateManager.json() + ",";
            metaData += GameConfig.json() + ",";
            metaData += "\"timestamp\":\"" + dateFormat.format(date) + "\"";
            metaData += "}";

            Logger.metaData(metaData);
        }
    }
}
