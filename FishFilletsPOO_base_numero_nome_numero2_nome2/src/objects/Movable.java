package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;

public abstract class Movable extends GameObject {

    public Movable(Point2D position) {
        super(position);
    }

    public void move(Direction d, Room r) {
        Point2D nextPos = getPosition().plus(d.asVector()); //aqui calculamos o destino: é a soma com qualquer direção escolhido pelo utilizador
        interactAt(nextPos, r); //pede room para definir as interações de cada objeto
        if(canMoveTo(nextPos, r))
            setPosition(nextPos); //isto pode dar erros de acesso à mesma lista e tentativa de mudança de muitos dados ao mesmo tempo: ConcurrentModificationException
    }

    public void gravity(Room r) { //é simplesmente para facilitar a chamada e distinguir entre move DOWNs e os outros
        move(Direction.DOWN, r); //pode mover-se para baixo //não tenho de fazer verificações aqui, o canMoveTo faz
    }

    public boolean canMoveTo(Point2D p, Room r) { //aqui é nos dado o ponto de destino, calculado em move
        GameObject o = r.getObject(p); //objeto que está no destino, neste caso na posição abaixo
        //System.out.println("o: " + o);
        if(o != null)  //se não houver água, ou seja, se o objeto sólido existir, para de cair. assim, move() não altera a posição, pois impede de chamar o setPosition
            return false;
        else if(o instanceof Tronco && this.getWeight().equals(Weight.HEAVY)) r.removeObject((Tronco) o);
        return true;
    }
    
    @Override
    public String getName() {
        return "movable";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    //métodos obrigatórios da classe que extendem Movable
    public abstract void interactAt(Point2D p, Room r); //interação do objeto que move com outros objetos
    public abstract Weight getWeight(); //saber o peso de cada objeto
}
