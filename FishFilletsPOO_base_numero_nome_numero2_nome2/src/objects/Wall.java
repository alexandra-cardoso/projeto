package objects;

//import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Wall extends GameObject{

    public Wall(Point2D ponto) {
        super(ponto);
    }

    @Override
    public String getName() {
        return "wall";
    }

    @Override
    public int getLayer() {
        return 1;
    }
    
}
