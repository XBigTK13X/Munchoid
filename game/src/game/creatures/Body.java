package game.creatures;

import sps.core.Point2;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<BodyPart> parts;
    private static int partWidthMin = 25;
    private static int partHeightMin = 25;

    private Point2 position;

    public Body(int numberOfParts, int partWidthMax, int partHeightMax) {
        position = Point2.random();
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
}
