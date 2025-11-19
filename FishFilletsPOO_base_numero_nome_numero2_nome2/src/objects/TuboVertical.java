package objects;

import pt.iscte.poo.utils.Point2D;

public class TuboVertical extends GameObject{

    public TuboVertical(Point2D p) {
        super(p);
    }

    @Override
    public String getName() {
        return "steelVertical";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    
}