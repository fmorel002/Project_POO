package simpleUIApp;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.ubordeaux.simpleUI.*;

public class Main {
	public static void main(String[] args) {
		Random random = new Random();
		ArrayList<Item> testItemList = new ArrayList<Item>();
		ArrayList<SpaceShip> testItem = new ArrayList<SpaceShip>();
		Planet arthur = new Planet(random.nextInt(400), random.nextInt(500), 50, 1.0);
		Planet marcus = new Planet(0, 0, 0, 1.0);
		boolean wellPlaced = false;
		while(!wellPlaced){
			int x, y;
			x = random.nextInt(400);
			y = random.nextInt(500);
			if(arthur.getLocation().distance(new Point2D.Double(x, y)) <= 50){
				System.out.println("Marcus too close");
			}else{
				marcus.getLocation().setLocation(x, y);
				marcus.setWidth(50);
				wellPlaced = true;
			}
		}
		/*
		 * Randomly position 25*2 Ships in the Arena zone (defined afterwards)
		 */
		for (int i = 0; i < 25; i++) {
			testItemList.add(new SpaceShip(random.nextInt(400), random.nextInt(500), 10, marcus));
			testItem.add(new SpaceShip(random.nextInt(400), random.nextInt(500), 10, arthur));
		}
		arthur.setShipsAllPlanets(testItem);
		marcus.setShipsAllPlanets(testItem);
		testItemList.add(arthur);
		testItemList.add(marcus);
		testItemList.addAll(testItem);
		Manager manager = new Manager();
		Run r = new Run(400, 500);

		/*
		 * Call the run method of Application providing an initial item Collection, an
		 * item manager and an ApplicationRunnable
		 */
		Application.run(testItemList, manager, r);
	}
}
