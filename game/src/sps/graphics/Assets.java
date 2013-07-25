package sps.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import sps.bridge.SpriteType;
import sps.bridge.SpriteTypes;
import sps.core.Loader;
import sps.core.Logger;
import sps.util.Parse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Assets {
    private enum Sprites {
        Particle,
        Pixel,
        MenuBase
    }

    private static Assets instance;
    private static final String __menuBaseSprite = "MenuBase.png";
    private static final String __pixelSprite = "Pixel.png";

    public static Assets get() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    private final BitmapFont _font;

    private Map<String, Texture> textures = new HashMap<String, Texture>();
    private Map<Sprites, Sprite> sprites = new HashMap<Sprites, Sprite>();

    private final HashMap<Integer, HashMap<Integer, Sprite>> indexedSprites = new HashMap<Integer, HashMap<Integer, Sprite>>();
    private final HashMap<Integer, String> spriteNames = new HashMap<Integer, String>();

    private Assets() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(new FileHandle(Loader.get().graphics("Molot.otf")));
        _font = generator.generateFont(60);
        generator.dispose();

        for (File spriteTile : Loader.get().graphics("sprites").listFiles()) {
            if (!spriteTile.isHidden()) {
                String[] comps = spriteTile.getName().split("-");
                int index = Parse.inte(comps[0]);
                int frame = Parse.inte(comps[1]);
                String name = "";
                for (int ii = 2; ii < comps.length; ii++) {
                    String baseName = comps[ii].replace(".png", "");
                    name += Character.toUpperCase(baseName.charAt(0)) + baseName.substring(1) + "_";
                }
                name = name.substring(0, name.length() - 1);
                spriteNames.put(index, name);
                Sprite sprite = new Sprite(new Texture(spriteTile.getAbsolutePath()));
                if (!indexedSprites.containsKey(index)) {
                    indexedSprites.put(index, new HashMap<Integer, Sprite>());
                }
                if (!indexedSprites.get(index).containsKey(frame)) {
                    indexedSprites.get(index).put(frame, sprite);
                }
            }
        }

        for (Integer index : indexedSprites.keySet()) {
            int frames = indexedSprites.get(index).keySet().size();
            String id = spriteNames.get(index);
            SpriteTypes.add(new SpriteType(id, index, frames));
        }

        try {
            sprites.put(Sprites.MenuBase, new Sprite(image(__menuBaseSprite)));
        }
        catch (Exception e) {
            Logger.exception("ERROR: Exception while loading the menu base sprite. The HUDs that use it might not be functional.", e, false);
        }

        try {
            sprites.put(Sprites.Pixel, new Sprite(image(__pixelSprite)));
        }
        catch (Exception e) {
            Logger.exception("ERROR: Exception while loading the menu base sprite. The HUDs that use it might not be functional.", e, false);
        }
    }

    private FileHandle fragmentShader() {
        return new FileHandle(Loader.get().graphics("fragment.glsl"));
    }

    private FileHandle vertexShader() {
        return new FileHandle(Loader.get().graphics("vertex.glsl"));
    }

    public ShaderProgram defaultShaders() {
        ShaderProgram shaders = new ShaderProgram(vertexShader(), fragmentShader());
        if (!shaders.isCompiled()) {
            Logger.exception(new Exception("Shader compilation failed. GLSL log:\n\t" + shaders.getLog()));
        }
        return shaders;
    }

    public File markovSeed() {
        return Loader.get().data("markov-seed.txt");
    }

    public BitmapFont font() {
        return _font;
    }

    public Texture image(String fileName) {
        if (!textures.containsKey(fileName)) {
            textures.put(fileName, new Texture(Loader.get().graphics(fileName).getAbsolutePath()));
        }
        return textures.get(fileName);
    }

    public Sprite sprite(int verticalIndex) {
        return indexedSprites.get(verticalIndex).get(0);
    }

    public Sprite sprite(int frame, int index) {
        return indexedSprites.get(index).get(frame);
    }

    public Sprite baseMenu() {
        return sprites.get(Sprites.MenuBase);
    }

    public Sprite pixel() {
        return sprites.get(Sprites.Pixel);
    }
}
