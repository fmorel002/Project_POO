package simpleUIApp;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Serializable;

import simpleUIApp.Planet.PlanetType;

class SpaceShip extends Entity implements Serializable{

	private Item objective;
	private Planet belongs;
	private boolean IA;
	private boolean isMoving;
	
	public SpaceShip(double x, double y, int w, Planet p) {
		super(x, y, w, new Color(119,136,153));
		isMoving = false;
		
		if(p.getType() == PlanetType.PLAYER)
		{
			this.setColor(new Color(46,139,87));
			this.IA = false;
		}
			
		else if((p.getType() == PlanetType.IA))
		{
			this.setColor(new Color(165,42,42));
			this.IA = true;
		}
			
		objective = this;
		belongs = p;
	}

	public Planet getBelongs() {
		return belongs;
	}
	
	public boolean getMoving() {
		return isMoving;
	}
	
	public Item getObjective() {
		return objective;
	}
	
	public boolean IsIaShip()
	{
		return IA;
	}

	public void setBelongs(Planet belongs) {
		this.belongs = belongs;
	}

	public void setObjective(Item o) {
		this.objective = o;
	}

	public void move() {
		if (!objective.contains(this.center)) {
			isMoving = true;
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
			
			Point2D tmp = new Point2D.Double(newx,newy);
			if(Planet.getPlanetFromCoord(tmp) == null)
			{
				center.setLocation(newx, newy);
			}
			else if(objective == Planet.getPlanetFromCoord(tmp)){
				center.setLocation(newx, newy);
			}
			//New PathFinding
			else{
				double planX = Planet.getPlanetFromCoord(tmp).getLocation().getX();
				double planY = Planet.getPlanetFromCoord(tmp).getLocation().getY();
				int planWidth = Planet.getPlanetFromCoord(tmp).getWidth();

				if(planX > newx && planY + planWidth > newy){
					center.setLocation(newx - 3, newy - 5); // -1 avant
					//System.out.println("route 1 path finding");
				}
				else if(planX + planWidth > newx && planY + planWidth > newy){
					center.setLocation(newx + 2, newy - 3); // - 4 possible aussi
					//System.out.println("route 2 path finding");
				}
				else if(planX + planWidth < newx && planY + planWidth < newy){
					center.setLocation(newx + 1, newy - 3);
					//System.out.println("route 3 path finding");
				}
				else if(planX + planWidth > newx && planY + planWidth < newy){
					center.setLocation(newx - 1, newy - 3);
					//System.out.println("route 4 path finding");
				}
			}
		} else {
			//Quand bâteau est arrivé sur la planète.
			isMoving = false;
			objective = this;
			this.belongs = Planet.getPlanetFromCoord(this.getLocation());
		}
	}
}
