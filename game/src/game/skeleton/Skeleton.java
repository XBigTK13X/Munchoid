package game.skeleton;

import sps.core.Point2;
import sps.util.Screen;

public class Skeleton {

    private Joint root;

    public Skeleton(){

    }

    public Skeleton(int joints) {
        int boneLength = 30;

        root = new Joint(Screen.width(50), Screen.height(50));
        Joint joint = null;
        while (joints > 0) {
            Point2 pos = Screen.rand(10, 90, 10, 90);
            joints--;
            Joint temp = new Joint(pos.X, pos.Y);
            if (root.getOutbounds().size() == 0) {
                joint = temp;
                root.addOutbound(joint);
                joint.setInbound(root);
            }
            else {
                joint.addOutbound(temp);
                temp.setInbound(joint);
                joint = temp;
            }
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Joint parent){
        if(parent.getOutbounds().size() > 0){
            for(Joint j:parent.getOutbounds()){
                draw(j);
            }
        }
        parent.draw();
    }

    public void update() {
        update(root);
    }

    public void update(Joint parent){
        if(parent.getOutbounds().size() > 0){
            for(Joint j:parent.getOutbounds()){
                update(j);
            }
        }
        parent.update();
    }
}
