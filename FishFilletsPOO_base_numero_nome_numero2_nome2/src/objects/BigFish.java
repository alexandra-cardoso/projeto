package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class BigFish extends GameCharacter{

	public BigFish(Point2D p) {
		super(p);
	}
	
	@Override
	public String getName() {
		return getFacing() ? "bigFishLeft" : "bigFishRight";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public boolean canSupport(Movable o) {
		if(o.getWeight().equals(Weight.LIGHT)) return true; //sim se objeto leve
		else if(o.getWeight().equals(Weight.HEAVY)) { //se objeto pesado
			if(super.getSuported() != null && super.getSuported().getWeight().equals(Weight.HEAVY)) return false; //se está a suportar objeto e ele é pesado, não pode suportar outro
			else if(super.getSuported() == null) return true; //se não está a suportar outro objeto
		}
		super.setSuports(o);
		return false;
	}

	@Override
	public boolean canPush(Movable o, Direction d) {
		if(d.equals(Direction.LEFT) || d.equals(Direction.RIGHT)) return true;
		else if (d.equals(Direction.UP) && super.getSuported() == null) return true; //se é na vertical e não está a suportar um objeto anterior
		return false; //em todas as outras situações, não empurra
	}
}
