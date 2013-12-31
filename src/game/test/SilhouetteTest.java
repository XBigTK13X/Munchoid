package game.test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.core.InitialStateResolver;
import game.core.InputWrapper;
import game.creatures.PartFunction;
import game.creatures.part.Design;
import game.creatures.part.Designs;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.core.Logger;
import sps.core.RNG;
import sps.display.Window;
import sps.draw.Outline;
import sps.draw.SpriteMaker;
import sps.states.State;
import sps.states.StateManager;

public class SilhouetteTest implements State {
    private Sprite _silhouette;
    private Color[][] _base;

    @Override
    public void create() {
        rebuildSprite();
    }

    private void rebuildSprite() {
        Design design = Designs.get(RNG.pick(PartFunction.values()));
        int[][] designBase = design.create(300, 300);
        _base = Designs.toColors(designBase, Color.GREEN);
        _silhouette = SpriteMaker.fromColors(_base);
    }

    @Override
    public void draw() {
        Window.get().schedule(_silhouette, DrawDepths.get("Default"));
    }

    @Override
    public void update() {
        if (InputWrapper.pop()) {
            rebuildSprite();
        }
        if (InputWrapper.push()) {
            Designs.rebuildIndex();
            Logger.info("Rebuilt");
        }
        if (InputWrapper.debug2()) {
            Outline.single(_base, Color.WHITE, 2);
            _silhouette = SpriteMaker.fromColors(_base);
        }
        if (InputWrapper.confirm()) {
            StateManager.reset().push(InitialStateResolver.create());
        }
        if (InputWrapper.debug1()) {
            StateManager.reset().push(new SkeletonTest());
        }
    }

    @Override
    public void asyncUpdate() {

    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void pause() {

    }
}
