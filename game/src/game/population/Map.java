package game.population;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Window;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;

public class Map {
    public static final int __c = 255;

    private static enum Biome {
        Land(true, __c / 2, 180),
        Desert(true, 180, __c),
        Water(false, __c / 3, __c / 2),
        Ice(true, 0, __c / 3);

        private static final int __colorMin = 10;
        private static final int __colorMax = 245;

        public final boolean Habitable;
        public final int ElevationMax;
        public final int ElevationMin;

        private Biome(boolean habitable, int elevationMin, int elevationMax) {
            Habitable = habitable;
            ElevationMax = elevationMax;
            ElevationMin = elevationMin;
        }

        public static Biome fromElevation(int elevation) {
            for (Biome b : values()) {
                if (b.ElevationMin <= elevation && b.ElevationMax >= elevation) {
                    return b;
                }
            }
            throw new RuntimeException("Undefined biome elevation!");
        }

        public static Color getColor(int elevation) {
            int r = 0;
            int g = 0;
            int b = 0;
            switch (fromElevation(elevation)) {
                case Land:
                    g = elevation;
                    break;
                case Desert:
                    r = g = elevation;
                    b = elevation - 50;
                    break;
                case Water:
                    b = __c - elevation;
                    break;
                case Ice:
                    r = g = __c - elevation / 3 - 30;
                    b = __c - elevation / 2 + 30;
                    break;
            }

            return Colors.rgb(cap(r), cap(g), cap(b), 255);
        }

        private static int cap(int colorComponent) {
            return Math.min(Math.max(__colorMin, colorComponent), __colorMax);
        }
    }

    private Sprite _sprite;
    private Sprite _bg;
    private int[][] _habitableZones;
    private Point2 _position;

    private static final int __habitBuffer = 25;

    public Map(int width, int height, Point2 position) {
        _position = position;
        Color[][] base = ProcTextures.perlin(width, height, Colors.rgb(0, 255, 255), Colors.rgb(255, 255, 255), 9, true);
        _habitableZones = new int[width][height];

        if (GameConfig.OptSmoothRegionMap) {
            ProcTextures.blur(base, 2);
        }

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                int elevation = (int) (255 * base[ii][jj].r);
                base[ii][jj] = Biome.getColor(elevation);
                if (ii > __habitBuffer && ii < width - __habitBuffer && jj > __habitBuffer && jj < height - __habitBuffer) {
                    _habitableZones[ii][jj] = Biome.fromElevation(elevation).Habitable ? 1 : 0;
                }
                else {
                    _habitableZones[ii][jj] = 0;
                }
            }
        }

        int frameSizePixels = 6;
        _bg = SpriteMaker.get().fromColors(ProcTextures.monotone(width + frameSizePixels, height + frameSizePixels, Color.WHITE));
        _bg.setPosition(position.X - frameSizePixels / 2, position.Y - frameSizePixels / 2);
        _sprite = SpriteMaker.get().fromColors(base);
        _sprite.setPosition(position.X, position.Y);
    }

    public void resetSpace(Point2 location) {
        int x = (int) (location.X);
        int y = (int) (location.Y);
        _habitableZones[x][y] = 1;
    }

    private static final int requiredLivingSpace = 15;

    public Point2 getOpenSpace() {
        while (true) {
            Point2 location = new Point2(RNG.next(0, _habitableZones.length), RNG.next(0, _habitableZones[0].length));
            if (_habitableZones[(int) location.X][(int) location.Y] == 1) {
                boolean success = true;
                for (int ii = -requiredLivingSpace; ii < requiredLivingSpace; ii++) {
                    for (int jj = -requiredLivingSpace; jj < requiredLivingSpace; jj++) {
                        int x = (int) (ii + location.X);
                        int y = (int) (jj + location.Y);
                        if (x >= 0 && x < _habitableZones.length && y >= 0 && y < _habitableZones[0].length) {
                            if (_habitableZones[x][y] != 1) {
                                success = false;
                            }
                        }
                    }
                }
                if (success) {
                    _habitableZones[(int) location.X][(int) location.Y] = 2;
                    return location;
                }
            }
        }
    }

    public Point2 getPosition() {
        return _position;
    }

    public void draw() {
        Window.get().draw(_bg);
        Window.get().draw(_sprite);
    }
}
