package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.style.Outline;
import sps.display.Screen;
import sps.draw.Colors;
import sps.draw.SpriteMaker;

public class UI {
    public static Sprite button(Color color) {
        int width = (int) Screen.width(10);
        int height = (int) Screen.height(8);
        Color[][] base = Colors.genPerlinGrid(width, height, color, Color.BLACK, 7);
        Outline.single(base, Color.WHITE, GameConfig.MeterOutlinePixelThickness);
        return SpriteMaker.get().fromColors(base);
    }
}
