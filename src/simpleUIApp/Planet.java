package simpleUIApp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.geom.Point2D;

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
	
	
	/**
	 * @brief found a valid position to place a new planet
	 * @param width : width of the window
	 * @param height : height of the window
	 * @param planets : List of all planet on the map
	 * @param pos : pos of the new planet to generate
	 * 
	 */
	
	public static Point2D findPlanetPosition(int width, int height, ArrayList<Planet> planets, int planetSize)
	{
		final int nbTry = 1000; // nombre d'essai max pour trouver une pos valide
		final int spacing = planetSize +30; // on choisi l'espacement entre les planètes
		boolean isValidPos = true;
		Random random = new Random();
		Point2D pos = new Point2D.Double(0,0);
		
		int nbTryCpt = 0;
		
		do
		{	
			int x = random.nextInt((width - (planetSize/2)) + 1 - (planetSize/2)) + (planetSize/2);
			int y = random.nextInt((height - (planetSize/2)) + 1 - (planetSize/2)) + (planetSize/2);
			pos.setLocation(x,y);
			
			// Si c'est la première planete, son emplacement est forcement valide
			if(planets.size() == 0)
				isValidPos = true;
				
			
			for(Planet p : planets){
				if(p.getLocation().distance(pos) < spacing)
					isValidPos = false;
			}
			
			nbTryCpt++;
			
		} while(!isValidPos && nbTryCpt < nbTry);
		
		// Au bout de "nbTry" essais, on arrête de chercher une position valide et on affiche une erreur
		if(nbTryCpt == nbTry)
		{
			System.out.println("Impossible de trouver une position valide pour une nouvelle planete");
			pos.setLocation(-1, -1);
		}
		return pos;
			
	}
}
