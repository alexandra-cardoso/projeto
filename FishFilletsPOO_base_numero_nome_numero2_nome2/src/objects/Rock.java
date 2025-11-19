package objects;

import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Rock extends Movable{
    
    public Rock(Point2D p) {
        super(p);
    }

    @Override
    public String getName() {
        return "stone";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactAt(Point2D p, Room r) {
        List<GameObject> objetos = r.getObjects(); //vai pela lista de objetos e verifica se algum está por baixo dele
        for (GameObject c : objetos) {
            if(c instanceof Tronco && c.getPosition().equals(p.plus(Direction.DOWN.asVector()))) r.removeObject((Tronco) c); //se encontrar um tronco a descer, remove-o, pois a pedra é pesada
            else setPosition(getPosition());
        }
    }

    @Override
    public Weight getWeight() {
        return Weight.HEAVY;
    }
    
}
