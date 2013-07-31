package game.skeleton;

import game.creatures.BodyPart;
import sps.core.Point2;
import sps.display.Screen;

public class Skeleton {

    private Joint root;

    public Skeleton(BodyPart core) {
        root = new Joint(core);
        grow(core, root);
    }

    private void grow(BodyPart start, Joint parent) {
        if (start.getConnections().getChildren() != null) {
            if (start.getConnections().getChildren().size() > 0) {
                for (BodyPart part : start.getConnections().getChildren()) {
                    Joint out = new Joint(part);
                    parent.addOutbound(out);
                    grow(part, parent);
                }
            }
        }
    }

    public Skeleton(int joints) {
        root = new Joint(Screen.width(50), Screen.height(50));
        Joint joint = null;
        while (joints > 0) {
            Point2 pos = Screen.rand(10, 90, 10, 90);
            joints--;
            Joint temp = new Joint(pos.X, pos.Y);
            if (root.getOutbounds().size() == 0) {
                joint = temp;
                root.addOutbound(joint);
            }
            else {
                joint.addOutbound(temp);
                joint = temp;
            }
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Joint parent) {
        if (parent.getOutbounds().size() > 0) {
            for (Joint j : parent.getOutbounds()) {
                draw(j);
            }
        }
        parent.draw();
    }

    public void update() {
        update(root);
    }

    public void update(Joint parent) {
        if (parent.getOutbounds().size() > 0) {
            for (Joint j : parent.getOutbounds()) {
                update(j);
            }
        }
        parent.update();
    }
}
