package simpleUIApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Planet extends Entity{
	
	public enum PlanetType { PLAYER, IA, NEUTRAL;}
	
	private ArrayList<SpaceShip> shipsAllPlanets;
	private double speedProduction;
	private PlanetType type;
	
	public Planet(double x, double y, int w, double sP, PlanetType type) {
		
		super(x, y, w, new Color(119,136,153));
		this.type = type;
		if(type == PlanetType.PLAYER)
			this.setColor(new Color(46,139,87));
		else if(type == PlanetType.IA)
			this.setColor(new Color(165,42,42));
		
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
	
	public PlanetType getType()
	{
		return this.type;
	}
	
	@Override
	public void draw(Graphics2D arg0) {
		Point2D pos = this.center;
		int x = (int) pos.getX(), y = (int) pos.getY(), w = this.getWidth();
		arg0.setColor(c);
		//arg0.fillRect(x - w / 2, y - w / 2, w, w);
		arg0.fillOval(x - w / 2, y - w / 2, w, w);
		arg0.setColor(Color.black);
		String text = "0";
		arg0.drawString(text, (int)pos.getX() - arg0.getFontMetrics().stringWidth(text)/2, (int)pos.getY() +5);
	}
	
	
	/**
	 * @brief Found a valid position to place a new planet
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
