package game.save;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import sps.core.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Serialize {
    private static final Kryo __kryo;

    static {
        __kryo = new Kryo();
        __kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public static void toFile(Object input, File target) {
        try {
            Output output = new Output(new FileOutputStream(target));
            __kryo.writeObject(output, input);
            output.close();
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }

    public static <T> T fromFile(File target, Class<T> type) {
        try {
            Input input = new Input(new FileInputStream(target));
            T result = __kryo.readObject(input, type);
            input.close();
            return result;
        }
        catch (Exception e) {
            Logger.exception(e);
        }
        return null;
    }
}
