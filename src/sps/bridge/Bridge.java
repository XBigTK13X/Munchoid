package sps.bridge;

import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.display.Assets;

public class Bridge {
    private static boolean __setup = false;

    public static void setup(boolean enableGraphics) {
        if (!__setup) {
            try {
                if (enableGraphics) {
                    Assets.get();
                }
                for (String line : FileUtils.readLines(Loader.get().data("bridge.cfg"))) {
                    if (!line.contains("#")) {
                        String[] values = line.split(",");
                        String name = values[0];
                        if (name.equals("context")) {
                            String id = values[1];
                            Contexts.add(new Context(id));
                        }
                        if (name.equals("command")) {
                            String id = values[1];
                            String context = values[2];
                            Commands.add(new Command(id, Contexts.get(context)));
                        }
                        if (name.equals("drawDepth")) {
                            String id = values[1];
                            int depth = Integer.parseInt(values[2]);
                            DrawDepths.add(new DrawDepth(id, depth));
                        }
                        if (name.equals("entityType")) {
                            String id = values[1];
                            EntityTypes.add(new EntityType(id));
                        }
                        if (name.equals("actorType")) {
                            String id = values[1];
                            String spriteType = values[2];
                            boolean generatable = false;
                            if (values.length == 4) {
                                generatable = values[3].equals("true");
                            }
                            if (enableGraphics) {
                                ActorTypes.add(new ActorType(id, SpriteTypes.get(spriteType), generatable));
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                Logger.exception("Error occurred while parsing bridge.cfg.", e);
            }
            __setup = true;
        }
    }

    private Bridge() {

    }
}
