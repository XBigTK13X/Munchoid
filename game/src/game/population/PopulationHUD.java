package game.population;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.style.Outline;
import sps.core.Point2;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class PopulationHUD {
    private Population _population;

    private WorldMap _map;
    private Sprite _popIcon;
    private int _maxIconsWide;
    private int _maxIconsHigh;
    private int _maxIcons;
    private List<Point2> _iconPositions;
    private int _pad = 1;

    private Color _iconColor = new Color(Color.WHITE);

    public PopulationHUD(Population population, Point2 size, Point2 position) {
        _population = population;

        _map = new WorldMap((int) size.X, (int) size.Y, position);

        Color[][] popIconBase = ProcTextures.monotone((int) Screen.width(1), (int) Screen.width(1), _iconColor);
        Outline.single(popIconBase, Color.BLACK, 2);
        _popIcon = SpriteMaker.get().fromColors(popIconBase);

        _maxIconsHigh = (int) (size.Y / (_popIcon.getHeight() + _pad)) - 1;
        _maxIconsWide = (int) (size.X / (_popIcon.getWidth() + _pad)) - 1;
        _maxIcons = _maxIconsHigh * _maxIconsWide;

        _iconPositions = new ArrayList<>();
    }

    public void recalcIcons() {
        int iconsToDraw = MathHelper.clamp((int) (_maxIcons * ((float) _population.getSize()) / GameConfig.PopulationMax), 1, _maxIcons);

        while (iconsToDraw < _iconPositions.size()) {
            Point2 kill = _iconPositions.get(_iconPositions.size() - 1);
            _iconPositions.remove(_iconPositions.size() - 1);
            _map.resetSpace(kill.add(-_map.getPosition().X, _map.getPosition().Y).add(_popIcon.getWidth() / 2, _popIcon.getHeight() / 2));
        }

        while (iconsToDraw > _iconPositions.size()) {
            _iconPositions.add(_map.getOpenSpace().add(_map.getPosition()).add(-_popIcon.getWidth() / 2, -_popIcon.getHeight() / 2));
        }
    }

    public void draw() {
        _map.draw();
        for (Point2 p : _iconPositions) {
            _popIcon.setPosition(p.X, p.Y);
            _iconColor.a = 1;
            _popIcon.setColor(_iconColor);
            Window.get().draw(_popIcon);
        }
    }
}
