package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import sps.display.Screen;
import sps.draw.Colors;
import sps.draw.SpriteMaker;

public class UI {
    public static Sprite button(Color color, int screenWidthPercent, int screenHeightPercent) {
        int width = (int) Screen.width(screenWidthPercent);
        int height = (int) Screen.height(screenHeightPercent);
        Color[][] base = Colors.genPerlinGrid(width, height, color, Color.BLACK, 8);
        Outline.single(base, Color.WHITE, GameConfig.MeterOutlinePixelThickness);
        return SpriteMaker.get().fromColors(base);
    }

    public static Sprite button(Color color) {
        return button(color, 10, 8);
    }
}
