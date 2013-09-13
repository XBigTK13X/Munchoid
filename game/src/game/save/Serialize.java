package game.save;

import com.badlogic.gdx.utils.Json;
import org.apache.commons.io.FileUtils;
import sps.core.Logger;

import java.io.File;

public class Serialize {
    public static void toFile(Object input, File target) {
        try {
            Json json = new Json();
            String output = json.toJson(input);
            FileUtils.writeStringToFile(target, output);
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }

    public static <T extends Object> T fromFile(File target, Class<T> type) {
        try {
            Json json = new Json();
            String input = FileUtils.readFileToString(target);
            return json.fromJson(type, input);
        }
        catch (Exception e) {
            Logger.exception(e);
        }
        return null;
    }
}
