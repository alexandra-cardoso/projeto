package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.Water;
import objects.Anchor;
import objects.BigFish;
import objects.Bomb;
import objects.GameObject;
import objects.HoledWall;
import objects.Rock;
import objects.SmallFish;
import objects.SteelHorizontal;
import objects.Taça;
import objects.Trap;
import objects.Tronco;
import objects.TuboVertical;
import objects.GameCharacter;
import objects.Wall;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private List<GameObject> objects;
	private String roomName;
	
	private SmallFish sf;
	private BigFish bf;
	private GameCharacter active;
	
	public Room() {
		objects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	public void addObject(GameObject obj) {
		objects.add(obj); //adiciona à lista
		ImageGUI.getInstance().addImage(obj); //adiciona a imagem à Interface Gráfica
	}

	public void removeObject(GameObject obj) {
		objects.remove(obj);
		ImageGUI.getInstance().removeImage(obj);
	}

	public List<GameObject> getObjects() {
		return objects;
	}
	
	public SmallFish getSmallFish() {
		return sf;
	}
	
	public BigFish getBigFish() {
		return bf;
	}

	public GameCharacter getActiveFish() {
		return active;
	}
	
	public void setSmallFish(SmallFish sf) {
		this.sf = sf;
	}
	
	public void setBigFish(BigFish bf) {
		this.bf = bf;
	}
	
	public static Room readRoom(File f) {
		Room r = new Room();
		r.setName(f.getName());

		try (Scanner s = new Scanner(f)) { //assim o scanner fecha quando o try acaba, e não tem risco de ficar aberto
			int y = 0;

			while (s.hasNextLine()) {
				String linha = s.nextLine();

				for(int x = 0; x < linha.length(); x++) {
					Point2D p = new Point2D(x,y);
					
					r.addObject(new Water(p)); //água está em todo o tabuleiro

					char c = linha.charAt(x);
					//a partir daqui definimos a criação de objetos: pode estar no GameObject
					if(c == 'B') { //optei por mudar para um conjunto de ifs em vez de um switch para reduzir as linhas de código, apenas
						if(r.bf != null) throw new IllegalArgumentException("Impossível haver um jogo com 2 peixes grandes."); //impede que um jogo tenha mais que 1 peixe de cada tipo
						BigFish bf = new BigFish(p);
						r.setBigFish(bf);
						r.addObject(bf);
						r.active = bf;
					}
					else if(c == 'S') {
						if(r.sf != null) throw new IllegalArgumentException("Impossível haver um jogo com 2 peixes pequenos.");
						SmallFish sf = new SmallFish(p);
						r.setSmallFish(sf);
						r.addObject(sf);
					}
					else if(c == 'W') r.addObject(new Wall(p));
					else if(c == 'H') r.addObject(new SteelHorizontal(p));
					else if(c == 'V') r.addObject(new TuboVertical(p));
					else if(c == 'C') r.addObject(new Taça(p));
					else if(c == 'R') r.addObject(new Rock(p));
					else if(c == 'A') r.addObject(new Anchor(p));
					else if(c == 'b') r.addObject(new Bomb(p));
					else if(c == 'T') r.addObject(new Trap(p));
					else if(c == 'Y') r.addObject(new Tronco(p));
					else if(c == 'X') r.addObject(new HoledWall(p));
				}
				y++;
			}
		} catch(FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado");
		}
		return r;
	}

	public void switchActiveFish() {
		if(active == bf) active = sf;
		else if(active == sf) active = bf;
	}

	public void deactivateActiveFish() { //FALTA MODIFICAR ESTE MÉTODO PARA FICAR MAIS OTIMIZADO
		List<GameCharacter> peixes = new ArrayList<>(); //criei uma lista para percorrer os peixes e perceber o que está morto ou fora da sala
		peixes.add(sf); //podem ser null por terem sido mudados anteriormente
		peixes.add(bf);

		for (GameCharacter p : peixes) {
			if(p != null && (!p.isAlive() || p.isOut())) { //então aqui temos de verificar se estão mortos ou fora 
				removeObject(p); //depois removemos o objeto que está morto ou fora e fazemos as verificações todas para deixar null o que tiver saído
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
	}

	public GameObject getObject(Point2D p) { //p é o destino, passado no método canMoveTo() de um Movable
		for(GameObject o : objects) {
			if(o.getPosition().equals(p) && !(o instanceof Water)) return o; //aqui queremos saber se tem um objeto sólido nessa direção, a água é ignorada, pois não provoca reação adicionar no objeto
		}
		return null; //pode ter mais que um objeto na mesma posição: retornar lista de objetos na posição. Não porque se tiver um objeto já não se pode mover
	}
}