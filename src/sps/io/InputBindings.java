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
            String chord = "";
            if (command.keys().length == 1) {
                chord = command.keys()[0].name();
            }
            if (command.keys().length > 1) {
                for (int ii = 0; ii < command.keys().length; ii++) {
                    chord += command.keys()[ii];
                    if (ii < command.keys().length - 1) {
                        chord += "+";
                    }
                }
            }
            result.add(command.name() + "," + chord + "-" + command.controllerInput().serialize());
        }
        return result;
    }

    public static void fromConfig(List<String> config) {
        try {
            for (String line : config) {
                if (!line.contains("##") && line.length() > 1) {
                    String key = line.split(",")[0];
                    String value = line.split(",")[1];
                    String[] rawChord = value.split("-")[0].split("\\+");

                    Keys[] chord = new Keys[rawChord.length];
                    for (int ii = 0; ii < rawChord.length; ii++) {
                        chord[ii] = Keys.get(rawChord[ii]);
                    }

                    ControllerInput controllerInput = ControllerInput.parse(value.split("-")[1]);
                    //Unless otherwise defined in bridge.cfg already,
                    // init a new binding to always lock after 1 pressF
                    if (Commands.get(key) == null) {
                        Commands.add(new Command(key, Contexts.get(Sps.Contexts.All)));
                    }
                    Commands.get(key).bind(controllerInput, chord);

                }
            }
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }
}