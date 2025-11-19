package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Anchor extends Movable {

    public Anchor(Point2D p) {
        super(p);
    }
    
    @Override
    public String getName() {
        return "anchor";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void interactAt(Point2D p, Room r) { //interactAt recebe a posição com que queremos interagir então não preciso de percorrer a lista para ver se há um objeto debaixo de nós, mas temos de distinguir se está ao lado ou em cima
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
        //devo verificar também se cai em cima de outros objetos mas aí tenho de definir algo em GameObjects que me diga que objetos se suportam FALTA
    }

    @Override
    public Weight getWeight() {
        return Weight.HEAVY;
    }
    
}
