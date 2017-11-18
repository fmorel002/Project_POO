package simpleUIApp;

import java.awt.Color;
import java.util.ArrayList;

public class Planet extends Entity{
	
	private ArrayList<SpaceShip> shipsAllPlanets;
	private double speedProduction;
	
	public Planet(double x, double y, int w, double sP) {
		super(x, y, w, Color.red);
		this.speedProduction = sP;
	}

	public void setObjective(Item o) {
		for(SpaceShip s : shipsAllPlanets){
			if(s.getBelongs() == this){
				s.setObjective(o);
			}
		}
	}
	public void setShipsAllPlanets(ArrayList<SpaceShip> shipsAllPlanets) {
		this.shipsAllPlanets = shipsAllPlanets;
	}
}
