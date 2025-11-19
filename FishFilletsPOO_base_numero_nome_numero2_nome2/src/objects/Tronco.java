package objects;

import pt.iscte.poo.utils.Point2D;

public class Tronco extends GameObject{
    public Tronco(Point2D p) {
        super(p);
    }

    @Override
    public String getName() {
        return "trunk";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
