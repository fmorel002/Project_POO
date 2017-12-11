package simpleUIApp;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fr.ubordeaux.simpleUI.Application;
import fr.ubordeaux.simpleUI.ApplicationRunnable;
import fr.ubordeaux.simpleUI.Arena;
import fr.ubordeaux.simpleUI.TimerRunnable;
import fr.ubordeaux.simpleUI.TimerTask;
import simpleUIApp.Planet.PlanetType;

public class Run implements ApplicationRunnable<Item> {

	private int width;
	private int height;
	private boolean isGO;
	private boolean isWon;
	static ArrayList<Planet> planets;
	static ArrayList<Item> allItem;
	static ArrayList<SpaceShip> spaceShipsList;

	public Run(int width, int height, ArrayList<Planet> al, ArrayList<Item> all, ArrayList<SpaceShip> sp) {
		this.width = width;
		this.height = height;
		planets = al;
		allItem = all;
		spaceShipsList = sp;
	}

	@Override
	public void run(final Arena<Item> arg0, Collection<Item> arg1) {
		MouseListener mouseHandler = new MouseListener();

		/*
		 * We build the graphical interface by adding the graphical component
		 * corresponding to the Arena - by calling createComponent - to a JFrame.
		 */
		final JFrame frame = new JFrame("Test Arena");

		/*
		 * This is our KeyHandler that will be called by the Arena in case of key events
		 */
		KeyListener keyListener = new KeyListener(frame);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(arg0.createComponent(this.width, this.height, mouseHandler, keyListener));
		frame.pack();
		frame.setVisible(true);
		this.isWon = false;
		this.isGO = false;

		/*
		 * We initially draw the component
		 */
		arg0.refresh();

		/*
		 * We ask the Application to call the following run function every seconds. This
		 * method just refresh the component.
		 */
		Application.timer(13, new TimerRunnable() {
			public void run(TimerTask timerTask) {
				arg0.refresh();
				for (Item item : arg1){
					item.move();
				}
			}
		});
		
		Application.timer(10, new TimerRunnable() {
			public void run(TimerTask timerTask) {
				for (Planet p : planets){
					p.shipsArrived();
				}
			}
		});	
		
		// On met a jour les plan�tes et on teste la fin de partie
		Application.timer(Planet.getSpeedProduction(), new TimerRunnable() {
			public void run(TimerTask timerTask) {
				int cptIAP = 0, cptPP = 0;
				for (Planet p : planets) {
					p.update();
					
					// Si la partie n'est pas terminé
					if(!isGO)
					{
						if(p.getType() == PlanetType.IA)
							cptIAP++;
						else if(p.getType() == PlanetType.PLAYER)
							cptPP++;
						
					}
				}
				if((cptIAP == 0 || cptPP == 0) && isGO == false){
					isGO = true;
					
					if(cptPP == 0){
						isWon = false;
						System.out.println("** YOU LOSE !  **");
					}
						
					else{
						isWon = true;
						System.out.println("** YOU WIN !  **");
					}
				}
			}

		});
		
		// on met a jour le nombre de vaisseau sur les plan�tes neutres
		Application.timer(Planet.getSpeedProductionNeutral(), new TimerRunnable() {
			public void run(TimerTask timerTask) {
				for (Planet p : planets) {
					p.updateNeutral();
				}
			}

		});
		
		// On met a jour les actions de l'IA
		Application.timer(3000, new TimerRunnable() {
			public void run(TimerTask timerTask) {
				Planet.updateIA();
			}
		});

		Application.timer(5000, new TimerRunnable() {
			public void run(TimerTask timerTask) {
				createBarbares();
			}
		});
	}

	public void createBarbares(){
		if(Math.random() > 0.5){
			ArrayList<SpaceShip> tmp = new ArrayList<SpaceShip>();
			int nbBarbMax = 10;
			int nbBarbMin = 5;
			int numBarb = (int)(Math.random() * ((nbBarbMax + 1) - nbBarbMin)) + nbBarbMin;
			for(int i = 0; i < numBarb; i++){
				//On choisit aléatoirement la planète à attaquer
				Random rand = new Random();
				int planetObjectiveSelected = rand.nextInt(planets.size());

				//On choisit des coordonnées aléatoires pour les vaisseaux, qui ne soient pas sous une planète.
				boolean found = false;
				int nbTry = 1000;
				int xShip;
				int yShip;
				do {
					xShip = rand.nextInt(800);
					yShip = rand.nextInt(600);
					if(Planet.getPlanetFromCoord(new Point2D.Double(xShip, yShip)) == null){
						found = true;
					}
					nbTry--;
				}
				while(!found || (nbTry == 0));

				SpaceShip barb = new SpaceShip(xShip, yShip, 10);
				barb.setObjective(planets.get(planetObjectiveSelected));
				tmp.add(barb);
			}
			spaceShipsList.addAll(tmp);
			allItem.addAll(tmp);
		}
	}

	/**
	 * Créer un fichier de sauvegarde dans /saves dans le dossier du projet. On écrit dedans toutes les instances des classes sérializable contenu dans la liste allItem.
	 */
	public static void saveGame(){
		try {
			FileOutputStream outFile = new FileOutputStream("./src/saves/saved.map");
			try {
				ObjectOutputStream outStream = new ObjectOutputStream(outFile);
				for(Item p : allItem){
					outStream.writeObject(p);
				}
				outStream.close();
				outFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de recharger la partie contenue dans /saves. On vide toutes les listes avant de les remplir avec les instances de classes contenues dans le fichier.
	 */
	public static void loadGame(){
		planets.clear();
		allItem.clear();
		spaceShipsList.clear();
		try {
			FileInputStream inFile = new FileInputStream("./src/saves/saved.map");
			try {
				ObjectInputStream inStream = new ObjectInputStream(inFile);
				boolean over = false;
				Object o;
				while(!over){
					try{
						o = inStream.readObject();
						Item i = (Item) o;
						allItem.add(i);
						if(i instanceof Planet){
							Planet p = (Planet) i;
							planets.add(p);
						}
						if(i instanceof SpaceShip){
							SpaceShip s = (SpaceShip) i;
							spaceShipsList.add(s);
						}
					}
					catch(Exception e){
						over = true;
					}
				}
				inStream.close();
				inFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
