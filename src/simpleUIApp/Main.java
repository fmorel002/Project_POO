package simpleUIApp;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import fr.ubordeaux.simpleUI.*;
import simpleUIApp.Planet.PlanetType;

public class Main {
	public static void main(String[] args) {
		
		final int win_width = 800;
		final int win_height = 600;
		Random random = new Random();
		
		ArrayList<Item> allItemList = new ArrayList<Item>();
		ArrayList<SpaceShip> spaceShipList = new ArrayList<SpaceShip>();
		
		// On génère une liste de planètes
		ArrayList<Planet> listPlanets = new ArrayList<Planet>();
		
		// On initialise x planets
		for(int i = 0; i < 8; i++)
		{
			Point2D p = Planet.findPlanetPosition(win_width, win_height, listPlanets,50);
			
			if(p.getX() != -1)
			{	
				if( i == 0)
					listPlanets.add(new Planet(p.getX(), p.getY(), 50, 1.0, PlanetType.PLAYER));
				else if(i == 1)
					listPlanets.add(new Planet(p.getX(), p.getY(), 50, 1.0, PlanetType.IA));
				else
					listPlanets.add(new Planet(p.getX(), p.getY(), 50, 1.0, PlanetType.NEUTRAL));
					
				for (int j = 0; j < 10; j++) {
					
					// On créer une liste de vaisseau qu'on associe a la dernière plnaète crééer
					spaceShipList.add(new SpaceShip(random.nextInt(win_width), random.nextInt(win_height), 10, listPlanets.get(listPlanets.size()-1)));
					listPlanets.get(listPlanets.size()-1).setShipsAllPlanets(spaceShipList);
					allItemList.addAll(spaceShipList);
				}
			}
		}
		
		allItemList.addAll(listPlanets);
		allItemList.addAll(spaceShipList);
		Manager manager = new Manager();
		Run r = new Run(win_width, win_height);

		/*
		 * Call the run method of Application providing an initial item Collection, an
		 * item manager and an ApplicationRunnable
		 */
		Application.run(allItemList, manager, r);
	}
}
