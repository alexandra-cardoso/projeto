package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import objects.GameObject;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import objects.Movable;


public class GameEngine implements Observer {
	
	private Room currentRoom;
	private int lastTickProcessed = 0;

	private int level;
	private static GameEngine INSTANCE;
	
	private GameEngine() {
		level = 0;			
	}

	public static GameEngine getInstance() {
		if(INSTANCE == null) INSTANCE = new GameEngine();
		return INSTANCE;
	}
	
	public Room getRoom() {
		return currentRoom;
	}

	public void startGame() {
		currentRoom = Room.readRoom(new File("./rooms/room" + level + ".txt"));
	}

	@Override
	public void update(Observed source) {

		if (ImageGUI.getInstance().wasKeyPressed()) { 
			
			int k = ImageGUI.getInstance().keyPressed();
			if (k == KeyEvent.VK_R) resetRoom();
			if(k == KeyEvent.VK_SPACE){
				if(currentRoom.getBigFish() != null && currentRoom.getSmallFish() != null) currentRoom.switchActiveFish(); //quando !bigFish.isAlive, o bigFish fica a null, então aqui ia fazer GetBigFish.isAlive, mas deu exceção pois p getter deu null
			}
			
			if (Direction.isDirection(k)) {
				if(currentRoom.getActiveFish() != null) // a verificar por causa de quando morrem ou saiem da sala e ele dá update
					currentRoom.getActiveFish().move(Direction.directionFor(k).asVector(), currentRoom); 
			}
		}

		int t = ImageGUI.getInstance().getTicks();
		//System.out.println(t);
		while (lastTickProcessed < t) {
			processTick();
		}
		
		ImageGUI.getInstance().update(); //faz um repaint, então substitui a imagem do peixe se o facing/getName() (um implica o outro) mudar
	}

	private void processTick() { //assim a cada processo do Tick, faz isto
		lastTickProcessed++;
		for(GameObject m : new ArrayList<>(currentRoom.getObjects())){ //assim crio uma copia da lista para não lançar uma exceção de tentativa de mudança ao mesmo tempo
			if(m instanceof Movable) 
				((Movable)m).gravity(currentRoom); // isto cria um ciclo em que a gravidade está sempre a tentar atuar
		}
	}

	public void resetRoom() {
		ImageGUI.getInstance().clearImages();
		currentRoom = Room.readRoom(new File("./rooms/room" + level + ".txt"));
	}

	public void win() {
		level++;
		ImageGUI.getInstance().update();  //dei update para o último peixe sair mesmo do mapa antes da mensagem de passagem de nível
		ImageGUI.getInstance().showMessage("You Won", "You Won");
		ImageGUI.getInstance().clearImages(); //limpa o nível em que estava antes, para não ficar sobreposto
		currentRoom = Room.readRoom(new File("./rooms/room" + level + ".txt"));
	}

	public void lost() {
		ImageGUI.getInstance().showMessage("Game Over", "Game Over");
		ImageGUI.getInstance().clearImages();
		currentRoom = Room.readRoom(new File("./rooms/room" + level + ".txt"));
	}

	/*public void deactivateActiveFish() { //FALTA MODIFICAR ESTE MÉTODO PARA FICAR MAIS OTIMIZADO
		List<GameCharacter> peixes = new ArrayList<>(); //criei uma lista para percorrer os peixes e perceber o que está morto ou fora da sala
		BigFish bf = currentRoom.getBigFish();
		SmallFish sf = currentRoom.getSmallFish();
		peixes.add(bf); //podem ser null por terem sido mudados anteriormente
		peixes.add(sf);
		GameCharacter active = currentRoom.getActiveFish();

		for (GameCharacter p : peixes) {
			if(p != null && (!p.isAlive() || p.isOut())) { //então aqui temos de verificar se estão mortos ou fora 
				currentRoom.removeObject(p); //depois removemos o objeto que está morto ou fora e fazemos as verificações todas para deixar null o que tiver saído
				if(p == bf) bf = null;
				else if(p == sf) sf = null;
				if(active == p) { //se morreu o active
					if(bf != null) active = bf; //se o bf ainda estiver vivo
					else if(sf != null) active = sf; //se o sf ainda estiver vivo
					else active = null; //se já tinham morrido os dois
				}
			}
		}
		//System.out.println("active: " + active);
	}*/

}
