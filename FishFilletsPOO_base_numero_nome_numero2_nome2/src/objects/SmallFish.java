package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class SmallFish extends GameCharacter{

	public SmallFish(Point2D p) {
		super(p);
	}
	
	@Override
	public String getName() {
		return getFacing() ? "smallFishLeft" : "smallFishRight";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public boolean canSupport(Movable o) {
		if(o.getWeight().equals(Weight.HEAVY)) return false; //não se objeto pesado
		else if(o.getWeight().equals(Weight.LIGHT)) { //se objeto leve
			if(super.getSuported() != null) return false; //se já está a suportar objeto, não suporta mais
			else if(super.getSuported() == null) return true; //se não está a suportar outro objeto
		}
		super.setSuports(o);
		return false;
	}

	@Override
	public boolean canPush(Movable o, Direction d) {
		if(!(d.equals(Direction.DOWN)) && !super.isPushing() && o.getWeight().equals(Weight.LIGHT)) //se for horizontal ou vertical. se não estiver a empurrar mais nenhum. se for objeto leve
			return true; 
		return false;
	}
}
