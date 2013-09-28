package sps.io;

import org.apache.commons.io.FileUtils;
import sps.bridge.Command;
import sps.bridge.Commands;
import sps.bridge.Contexts;
import sps.bridge.Sps;
import sps.core.Loader;
import sps.core.Logger;

import java.util.ArrayList;
import java.util.List;

public class InputBindings {
    private static InputBindings __instance;

    public static void init() {
        if (__instance == null) {
            __instance = new InputBindings();
        }
    }

    public InputBindings() {
        Logger.info("Parsing input.cfg");
        try {
            fromConfig(FileUtils.readLines(Loader.get().data("input.cfg")));
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }

    public static List<String> toConfig() {
        List<String> result = new ArrayList<>();
        for (Command command : Commands.values()) {
            result.add(command.name() + "," + command.key().name() + "-" + command.controllerInput().serialize());
        }
        return result;
    }

    public static void fromConfig(List<String> config) {
        try {
            for (String line : config) {
                if (!line.contains("##") && line.length() > 1) {
                    String key = line.split(",")[0];
                    String value = line.split(",")[1];
                    Keys keyBinding = Keys.get(value.split("-")[0]);
                    ControllerInput controllerInput = ControllerInput.parse(value.split("-")[1]);
                    //Unless otherwise defined in bridge.cfg already,
                    // init a new binding to always lock after 1 pressF
                    if (Commands.get(key) == null) {
                        Commands.add(new Command(key, Contexts.get(Sps.Contexts.All)));
                    }
                    Commands.get(key).bind(controllerInput, keyBinding);

                }
            }
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }
}