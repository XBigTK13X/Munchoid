package game.skeleton;

import sps.core.Point2;
import sps.util.Screen;

public class Skeleton {

    private Joint root;

    public Skeleton(int joints) {
        int boneLength = 30;

        root = new Joint(Screen.width(50), Screen.height(50));
        Joint joint = null;
        while (joints > 0) {
            Point2 pos = Screen.rand(10, 90, 10, 90);
            joints--;
            Joint temp = new Joint(pos.X, pos.Y);
            if (root.getOutbound() == null) {
                joint = temp;
                root.setOutbound(joint);
                joint.Inbound = root;
            }
            else {
                joint.setOutbound(temp);
                temp.Inbound = joint;
                joint = temp;
            }
        }
    }

    public void draw() {
        Joint current = root;
        while (true) {
            current.draw();
            if (current.getOutbound() == null) {
                return;
            }
            current = current.getOutbound();
        }
    }

    public void update() {
        Joint current = root;
        while (true) {
            current.update();
            if (current.getOutbound() == null) {
                return;
            }
            current = current.getOutbound();
        }
    }
}
