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

		// On initialise x planets
		int i = 0;
		int cptPlanete = 0;
		while (i < 8) {
			Point2D p = Planet.findPlanetPosition(win_width, win_height, listPlanets, 50);

			if (p.getX() != -1) {
				if (cptPlanete == 0)
					listPlanets.add(new Planet(p.getX(), p.getY(), 50, 1.0, PlanetType.PLAYER, allItemList,listPlanets));
				else if (cptPlanete == 1)
					listPlanets.add(new Planet(p.getX(), p.getY(), 50, 1.0, PlanetType.IA, allItemList,listPlanets));
				else
					listPlanets.add(new Planet(p.getX(), p.getY(), 50, 1.0, PlanetType.NEUTRAL, allItemList,listPlanets));

				// Si ce n'est pas une plan�te neutre : on ajoute des vaisseaux
				/*if (listPlanets.get(listPlanets.size() - 1).getType() != PlanetType.NEUTRAL) {

					for (int j = 0; j < 10; j++) {
						
						// On cr�er une liste de vaisseaux qu'on associe � la derni�re plan�te cr�er
						spaceShipList.add(new SpaceShip(random.nextInt(win_width), random.nextInt(win_height), 10,
								listPlanets.get(listPlanets.size() - 1)));
					}
				}*/
				cptPlanete++;
			}
			i++;
		}

		// On associe � la plan�te sa liste de vaisseaux
		Planet.setShipsAllPlanets(spaceShipList);
		
		allItemList.addAll(listPlanets);
		allItemList.addAll(spaceShipList);

		Manager manager = new Manager();
		Run r = new Run(win_width, win_height,listPlanets);

		/*
		 * Call the run method of Application providing an initial item Collection, an
		 * item manager and an ApplicationRunnable
		 */
		Application.run(allItemList, manager, r);
	}
}
