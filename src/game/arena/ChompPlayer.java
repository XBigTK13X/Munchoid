package game.arena;

import org.apache.commons.io.FilenameUtils;
import sps.audio.SoundPlayer;
import sps.core.Loader;
import sps.core.RNG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChompPlayer {
    private static List<String> __chomps;

    public static void setup() {
        __chomps = new ArrayList<>();
        for (File chomp : Loader.get().sound("chomp").listFiles()) {
            String id = FilenameUtils.removeExtension(chomp.getName());
            SoundPlayer.get().add(id, "chomp/" + chomp.getName());
            __chomps.add(id);
        }
    }

    public static void play() {
        SoundPlayer.get().play(RNG.pick(__chomps));
    }
}
