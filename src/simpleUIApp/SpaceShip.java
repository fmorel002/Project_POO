package simpleUIApp;

import java.awt.Color;

import simpleUIApp.Planet.PlanetType;

class SpaceShip extends Entity {

	private Item objective;
	private Planet belongs;
	
	public SpaceShip(double x, double y, int w, Planet p) {
		super(x, y, w, new Color(119,136,153));
		
		if(p.getType() == PlanetType.PLAYER)
			this.setColor(new Color(46,139,87));
		else if((p.getType() == PlanetType.IA))
			this.setColor(new Color(165,42,42));
		
		objective = this;
		belongs = p;
	}

	public Planet getBelongs() {
		return belongs;
	}

	public void setBelongs(Planet belongs) {
		this.belongs = belongs;
	}

	public void setObjective(Item o) {
		this.objective = o;
	}

	public void move() {
		if (!objective.contains(this.center)) {
			double newx = center.getX();
			double newy = center.getY();
			if (newx > objective.getLocation().getX()) {
				newx--;
			} else {
				newx++;
			}
			if (newy > objective.getLocation().getY()) {
				newy--;
			} else {
				newy++;
			}
			center.setLocation(newx, newy);
		} else {
			objective = this;
		}
	}
}
