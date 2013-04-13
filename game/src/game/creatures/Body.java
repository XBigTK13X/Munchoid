package game.creatures;

import sps.core.Point2;
import sps.core.RNG;
import sps.graphics.Renderer;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<BodyPart> parts;
    private static int partWidthMin = 50;
    private static int partHeightMin = 50;

    private Point2 position;

    public Body(int numberOfParts, int partWidthMax, int partHeightMax) {
        position = new Point2(RNG.next((int) (Renderer.get().VirtualWidth * .15f), (int) (Renderer.get().VirtualWidth * .85f)), RNG.next((int) (Renderer.get().VirtualHeight * .15f), (int) (Renderer.get().VirtualHeight * .85f)));
        parts = new ArrayList<BodyPart>();
        for (int ii = 0; ii < numberOfParts; ii++) {
            parts.add(new BodyPart(RNG.next(partWidthMin, partWidthMax), RNG.next(partHeightMin, partHeightMax), this));
        }
    }

    public void draw() {
        for (BodyPart part : parts) {
            part.draw();
        }
    }

    public Point2 getPosition() {
        return position;
    }

    public BodyPart getRandomPart() {
        return parts.get(RNG.next(0, parts.size()));
    }

    public void update() {
        for (int ii = 0; ii < parts.size(); ii++) {
            if (!parts.get(ii).isAlive()) {
                parts.remove(ii);
            }
        }
    }

    public boolean isAlive() {
        return parts.size() > 0;
    }
}
