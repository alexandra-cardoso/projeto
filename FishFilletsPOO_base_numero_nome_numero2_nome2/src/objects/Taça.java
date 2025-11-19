package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Taça extends Movable{

    public Taça(Point2D p) {
        super(p);
    }

    @Override
    public String getName() {
        return "cup";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactAt(Point2D p, Room r) {
        GameObject o = r.getObject(p); //posição(do objeto que vai mover) + Direção
        
        //se não entrar no if, move-se normalmente pois não tem que fazer ação nenhuma

        if(o != null && (o instanceof GameCharacter)) { //se o objeto existir e for um Peixe
            if( ((GameCharacter) o) instanceof BigFish) { //se tocar num dos peixes, chama a função do peixe onde tocou e vê-se o peixe a consegue suportar
                BigFish bf = ((BigFish)o);
                if (!bf.canSupport(this))
                    bf.die(r); //precisamos do Room nesta função para matar o peixe no nível

            } else if(((GameCharacter) o) instanceof SmallFish) {
                SmallFish sf = ((SmallFish)o);
                if (!sf.canSupport(this)) 
                    sf.die(r);
            }
        }
    }

    @Override
    public Weight getWeight() {
        return Weight.LIGHT;
    }
    
}
