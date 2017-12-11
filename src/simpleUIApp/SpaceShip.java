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
	private int escadronID;
	private boolean isBarbare;
	
	public SpaceShip(double x, double y, int w, Planet p) {
		super(x, y, w, new Color(119,136,153));
		isMoving = false;
		this.escadronID = -1;
		
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

		isBarbare = false;
		objective = this;
		belongs = p;
	}

	public SpaceShip(double x, double y, int w) {
		super(x, y, w, new Color(0,0,0));
		isMoving = false;
		this.escadronID = -1;

		isBarbare = true;
		objective = this;
	}

	public boolean isBarbare(){ return isBarbare; }

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

	/**
	 *  Permet de mettre à jour les coodronnées d'un vaisseau pour atteindre son objectif. Dans cette fonction se trouve l'évitement des planètes. On met à jour les variables de classe s'il est arrivé.
	 */

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
				}
				else if(planX + planWidth > newx && planY + planWidth > newy){
					center.setLocation(newx + 2, newy - 3); // - 4 possible aussi
				}
				else if(planX + planWidth < newx && planY + planWidth < newy){
					center.setLocation(newx + 1, newy - 3);
				}
				else if(planX + planWidth > newx && planY + planWidth < newy){
					center.setLocation(newx - 1, newy - 3);
				}
			}
		} else {
			//Quand bâteau est arrivé sur la planète.
			isMoving = false;
			objective = this;
			this.belongs = Planet.getPlanetFromCoord(this.getLocation());
		}
	}

	public void setEscadronID(int waveID) {
		this.escadronID = waveID;
	}
	
	public int getEscadronID()
	{
		return this.escadronID;
	}

}
