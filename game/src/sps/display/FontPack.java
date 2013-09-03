package sps.display;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import sps.core.Loader;

import java.util.HashMap;
import java.util.Map;

public class FontPack {
    Map<String, Map<Integer, BitmapFont>> _fonts;
    private int _defaultSize;

    private static final String __defaultLabel = "default";

    public FontPack() {
        _fonts = new HashMap<>();
    }

    public BitmapFont getDefault() {
        return getFont(__defaultLabel, _defaultSize);
    }

    public void setDefault(String fontName, int pointSize) {
        _defaultSize = pointSize;
        cacheFont(__defaultLabel, fontName, pointSize);
    }

    public BitmapFont getFont(String label, Integer pointSize) {
        return _fonts.get(label).get(pointSize);
    }

    public void cacheFont(String label, String fontName, int pointSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(new FileHandle(Loader.get().font(fontName)));
        if (!_fonts.containsKey(label)) {
            _fonts.put(label, new HashMap<Integer, BitmapFont>());
        }
        if (!_fonts.get(label).containsKey(pointSize)) {
            BitmapFont font = generator.generateFont(pointSize);
            _fonts.get(label).put(pointSize, font);
        }
        generator.dispose();
    }
}
