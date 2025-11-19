package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {

	private boolean isAlive;
	private boolean isOut; //iniciado por default em false, no java, tal como o isSuporting e o isPushing, já que quando o objeto é criado não verifica nenhuma dessas condições
	private boolean facingL; //variável que define para onde se dirige o peixe, e assim o nome do objeto
	private boolean isSupporting; //para saber se o peixe em questão está a suportar algum objeto
	private Movable suports; //e que objeto está a suportar: vamos saber o peso!!
	private boolean isPushing; //saber se está a empurrar algo (só se aplica pro peixe pequeno)

	public GameCharacter(Point2D p) {
		super(p);
		isAlive = true;
		facingL = true;
		suports = null;
	}

	public Movable getSuported() { //apenas os que se mexem serão suportados
		if(isSupporting) return suports;
		return suports;
	}

	public boolean isPushing() { //como apenas o peixe pequeno tem restrições e é apenas de empurrar 1 e 1 só objeto, só precisamos de saber se está a empurrar algo para cancelar o canPush() do SmallFish
		return isPushing;
	}

	public void setSuports(Movable m) { //o objeto que está a suportar fica guardado aqui
		isSupporting = true;
		suports = m;
	}

	public boolean getFacing() {
		return facingL;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isOut(){
		return isOut;
	}

	public void move(Vector2D dir, Room r) {
		if(dir.equals(Direction.LEFT.asVector())) facingL = true; //ativa a variável facingL quando quer mover para a direita
		else if(dir.equals(Direction.RIGHT.asVector())) facingL = false;

		Point2D destination = super.getPosition().plus(dir);
		if(!ImageGUI.getInstance().isWithinBounds(destination)){
			setPosition(destination);
			leave(r);
		}

		if (ImageGUI.getInstance().isWithinBounds(destination) && ImageGUI.getInstance().isWithinBounds(getPosition())){
			GameObject o = r.getObject(destination); //só verifica se há um objeto no destino
			if((!(o instanceof HoledWall || o instanceof Trap || o instanceof Water)) && o.getPosition().equals(destination)) return;
				if((o instanceof HoledWall || o instanceof Trap) && o.getPosition().equals(destination) && r.getActiveFish() instanceof BigFish) return; //o comportamento é o mesmo para quando se encontram com uma armadilha ou com uma parede com buracos
				else if((o instanceof HoledWall || o instanceof Trap) && o.getPosition().equals(destination) && r.getActiveFish() instanceof SmallFish) setPosition(destination);
		}

		setPosition(destination);
	}

	public void die(Room r) {
		isAlive = false;
		r.deactivateActiveFish();//para deixar de ter o objeto na room logo
		
		//System.out.println(getName() + "is Alive? " + isAlive);

		if(r.getBigFish() == null || r.getSmallFish() == null) GameEngine.getInstance().lost(); //isto é feito aqui para distinguir os finais: entre sair da sala e morrerem os dois //aqui verificamos se algum dos peixes está a null e não se o activeFish está a null, porque o activeFish é mudado antes de fazermos esta verificação
	}
	
	public void leave(Room r) {
		isOut = true;
		r.deactivateActiveFish();//para sair logo o objeto da room depois de ir pra posição
		
		//System.out.println(getName() + "is Out? " + isOut);

		if(r.getActiveFish() == null) GameEngine.getInstance().win(); //aqui verificamos a cada saída se não há mais peixes na sala e se passámos o nível
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public abstract boolean canSupport(Movable o); //estes dois métodos serão implementados pelos peixes
	public abstract boolean canPush(Movable o, Direction d); //só são suportados e empurrados objetos móveis
}