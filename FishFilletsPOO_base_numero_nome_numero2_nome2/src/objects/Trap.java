package objects;

import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Trap extends Movable {
    
    public Trap(Point2D p) {
        super(p);
    }

    @Override
    public String getName() {
        return "trap";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactAt(Point2D p, Room r) { //regras trap: atravessada pelo peixe pequeno, se tocar no peixe grande, morre
        List<Point2D> pontos = getPosition().getNeighbourhoodPoints(); //pontos das posições em que toca a trap ao mexer se
        for(Point2D o : pontos) {
            GameObject ob = r.getObject(o);
            if(ob instanceof BigFish) ((BigFish) ob).die(r); //se for o peixe grande, morre logo
            if(ob instanceof Tronco) r.removeObject((Tronco) ob); //Trap é um objeto pesado. se cair em cima do tronco, destroi
            if(ob instanceof GameObject && !(ob instanceof Movable) && !(ob instanceof GameCharacter) && !(ob instanceof Wall)) r.removeObject(ob); //destrói/remove o objeto da sala
        }
    }

    @Override
    public Weight getWeight() {
        return Weight.HEAVY;
    }
    
}
