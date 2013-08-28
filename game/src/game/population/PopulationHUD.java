package game.population;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class PopulationHUD {
    private static final int __colorMin = 20;
    private static final int __colorMax = 235;
    //Elevation base (single color range in perlin noise)
    private static final int __c = 255;


    private static Color elevationToColor(int elevation) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (elevation < __c / 2) {
            if (elevation > __c / 4) {
                b = __c - elevation;
            }
            else {
                r = g = __c - elevation / 3 - 20;
                b = __c - elevation / 3 - 10;
            }
        }
        else {
            if (elevation > 180) {
                r = g = elevation;
                b = elevation - 50;
            }
            else {
                g = elevation;
            }
        }

        return Colors.rgb(cap(r), cap(g), cap(b));
    }

    private static int cap(int colorComponent) {
        return Math.min(Math.max(__colorMin, colorComponent), __colorMax);
    }

    private static Color[][] world(int width, int height) {
        Color[][] base = ProcTextures.perlin(width, height, Colors.rgb(0, 255, 255), Colors.rgb(255, 255, 255), 9);

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                int elevation = (int) (255 * base[ii][jj].r);
                base[ii][jj] = elevationToColor(elevation);
            }
        }

        return base;
    }

    private Population _population;

    private Sprite _bg;
    private Sprite _popIcon;
    private int _maxIconsWide;
    private int _maxIconsHigh;
    private int _maxIcons;
    private List<Point2> _iconPositions;
    private int _pad = 1;

    public PopulationHUD(Population population, Point2 size, Point2 position) {
        _population = population;

        Color[][] bgBase = PopulationHUD.world((int) size.X, (int) size.Y);
        _bg = SpriteMaker.get().fromColors(bgBase);
        _bg.setPosition(position.X, position.Y);

        Color[][] popIconBase = ProcTextures.monotone((int) Screen.width(1), (int) Screen.width(1), Color.WHITE);
        Outline.single(popIconBase, Color.BLACK, 1);
        _popIcon = SpriteMaker.get().fromColors(popIconBase);

        _maxIconsHigh = (int) (_bg.getHeight() / (_popIcon.getHeight() + _pad)) - 1;
        _maxIconsWide = (int) (_bg.getWidth() / (_popIcon.getWidth() + _pad)) - 1;
        _maxIcons = _maxIconsHigh * _maxIconsWide;
    }

    public void recalcIcons() {
        _iconPositions = new ArrayList<>();

        int iconsToDraw = MathHelper.clamp((int) (_maxIcons * ((float) _population.getSize()) / GameConfig.PopulationMax), 1, _maxIcons);

        int iconCount = 0;
        for (int ii = 0; ii < _maxIconsHigh; ii++) {
            for (int jj = 0; jj < _maxIconsWide && iconCount < iconsToDraw; jj++) {
                iconCount++;
                _iconPositions.add(new Point2(_bg.getX() + jj * _popIcon.getWidth() + _popIcon.getWidth() / 2 + _pad * jj, _bg.getY() + ii * _popIcon.getHeight() + _popIcon.getHeight() / 2 + _pad * ii));
            }
        }
    }

    public void draw() {
        Window.get().draw(_bg);
        for (Point2 p : _iconPositions) {
            _popIcon.setPosition(p.X, p.Y);
            Window.get().draw(_popIcon);
        }
    }
}
