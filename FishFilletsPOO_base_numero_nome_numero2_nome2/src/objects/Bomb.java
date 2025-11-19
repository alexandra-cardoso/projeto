package objects;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends Movable {

    List<Point2D> afetados = new ArrayList<>();

    public Bomb(Point2D p) {
        super(p);
    }

    @Override
    public String getName() {
        return "bomb";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactAt(Point2D p, Room r) { //lembrando que esta função recebe a posição com que vai interagir e não onde está o objeto
        GameObject o = r.getObject(p); //posição(do objeto que vai mover) + Direção
        if(o != null && !(o instanceof GameCharacter)) explode(r); //explode quando em contacto com todos os objectos exceto peixes
        else if(o != null && o instanceof GameCharacter) { //interação com GameCharacters
            if(r.getBigFish() != null && r.getBigFish().getPosition().equals(p)) r.getBigFish().die(r); //verificamos sempre se o peixe não é nulo- morreu ou saiu da sala
            if(r.getSmallFish() != null || r.getSmallFish().getPosition().equals(p.plus(new Vector2D(0, -1)))) r.getSmallFish().die(r);
        }
    }

    public void explode(Room r) { //aqui o ponto usado é a posição do objeto então não precisamos de receber a proxima posição
        afetados = getPosition().getNeighbourhoodPoints();
        for(Point2D o : afetados) {
            GameObject ob = r.getObject(o); 
            if(ob instanceof GameCharacter) ((GameCharacter) ob).die(r); //se for um peixe, morre logo
            if(ob instanceof GameObject && !(ob instanceof Movable) && !(ob instanceof GameCharacter)/*  && !(ob instanceof Wall)*/) r.removeObject(ob); //destrói/remove o objeto da sala
        }
    }

    @Override
    public Weight getWeight() {
        return Weight.LIGHT;
    }
    
}
