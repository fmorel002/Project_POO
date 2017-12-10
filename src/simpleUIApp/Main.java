package simpleUIApp;

import java.awt.geom.Point2D;
import java.util.ArrayList;


import fr.ubordeaux.simpleUI.*;
import simpleUIApp.Planet.PlanetType;

public class Main {
	public static void main(String[] args) {

		final int win_width = 800;
		final int win_height = 600;

		ArrayList<Item> allItemList = new ArrayList<Item>();
		ArrayList<SpaceShip> spaceShipList = new ArrayList<SpaceShip>();
		ArrayList<Planet> listPlanets = new ArrayList<Planet>();
		
		// On associe � la plan�te sa liste de vaisseaux
		Planet.setShipsAllPlanets(spaceShipList);
		// On initialise le temps de création pour les vaisseaux de base sur les planètes.
		// et une width aléatoire pour toutes les planètes
		Planet.setPlanetProduction();
		Planet.setPlanetProductionNeutral();

		// On initialise x planets
		int i = 0;
		int cptPlanete = 0;
		while (i < 8) {
			int planetSize = Planet.generatePlanetWidth();
			Point2D p = Planet.findPlanetPosition(win_width, win_height, listPlanets,planetSize);

			//On s'assure d'avoir une planète joueur et IA au moins.
			if (p.getX() != -1) {
				if (cptPlanete == 0)
					listPlanets.add(new Planet(p.getX(), p.getY(), planetSize, PlanetType.PLAYER, allItemList,listPlanets));
				else if (cptPlanete == 1)
					listPlanets.add(new Planet(p.getX(), p.getY(),planetSize, PlanetType.IA, allItemList,listPlanets));
				else
					listPlanets.add(new Planet(p.getX(), p.getY(),planetSize, PlanetType.NEUTRAL, allItemList,listPlanets));

				cptPlanete++;
			}
			i++;
		}

		allItemList.addAll(listPlanets);
		allItemList.addAll(spaceShipList);

		Manager manager = new Manager();
		
		//On passe en param�tre listPlanets à Run pour faire les updates (augmenter nombre vaisseaux, vérifier si win ou pas)
		Run r = new Run(win_width, win_height, listPlanets, allItemList, spaceShipList);

		/*
		 * Call the run method of Application providing an initial item Collection, an
		 * item manager and an ApplicationRunnable
		 */
		Application.run(allItemList, manager, r);
	}
}
