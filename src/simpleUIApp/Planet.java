package simpleUIApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import java.awt.geom.Point2D;

public class Planet extends Entity implements Serializable{

	public enum PlanetType {
		PLAYER, IA, NEUTRAL;
	}

	private static ArrayList<SpaceShip> shipsAllPlanets;
	private static ArrayList<Item> allItem;
	private static ArrayList<Planet> allPlanets;
	
	private final static int speedProductionMax = 1000;
	private final static int speedProductionMin = 300;
	private static int speedProduction;
	
	private final static int speedProductionNeutralMax = 2000;
	private final static int speedProductionNeutralMin = 1500;
	private static int speedProductionNeutral;
	
	private final static int widthMax = 70;
	private final static int widthMin = 40;
	private int width;
	
	private PlanetType type;
	private int nbShip = 0;

	public Planet(double x, double y, int width, PlanetType type, ArrayList<Item> it,
				  ArrayList<Planet> planets) {

		super(x, y, width, new Color(119, 136, 153));
		this.type = type;
		if (type == PlanetType.PLAYER)
			this.setColor(new Color(46, 139, 87));
		else if (type == PlanetType.IA)
			this.setColor(new Color(165, 42, 42));


		allItem = it;
		allPlanets = planets;

	}
	
	public static int generatePlanetWidth(){
		Random r = new Random();
		return r.nextInt(widthMax - widthMin) + widthMin;
	}
	
	public int getPlanetWidth(){
		return width;
	}

	public void setObjective(Item o) {
		for (SpaceShip s : shipsAllPlanets) {
			if (s.getBelongs() == this) {
				if(!s.getMoving())
					s.setObjective(o);
			}
		}
	}
	
	public static void setPlanetProduction(){
		Random r = new Random();
		speedProduction = r.nextInt(speedProductionMax - speedProductionMin) + speedProductionMin;
	}
	
	public static int getSpeedProduction(){
		return speedProduction;
	}
	
	public static void setPlanetProductionNeutral(){
		Random r = new Random();
		speedProductionNeutral  = r.nextInt(speedProductionNeutralMax - speedProductionNeutralMin) + speedProductionNeutralMin;
	}
	
	public static int getSpeedProductionNeutral(){
		return speedProductionNeutral;
	}

	public static void setShipsAllPlanets(ArrayList<SpaceShip> ships) {
		shipsAllPlanets = ships;
	}

	public PlanetType getType() {
		return this.type;
	}
	
	public void shipsArrived(){
		
		Iterator<SpaceShip> it = shipsAllPlanets.iterator();
		
		while(it.hasNext()){
			SpaceShip ss = it.next();
				if (ss.getBelongs() == this) { 
								
				// Si un ship arrive sur une planète on le
				if(this.contains(ss.getLocation()) && !ss.getMoving())
				{
					// Si c'est un vaisseau du joueur
					if(!ss.IsIaShip())
					{
						if (this.type == PlanetType.PLAYER)
						{
							this.nbShip ++;
						}
						else if(this.type == PlanetType.IA || this.type == PlanetType.NEUTRAL){
							this.nbShip--;
							if(this.nbShip < 0){
								this.type = PlanetType.PLAYER;
								this.setColor(new Color(46, 139, 87));
								this.nbShip = this.nbShip *(-1);
							}
						}
					}
					// Si c'est un vaisseau de l'IA
					else
					{
						if (this.type == PlanetType.IA)
						{
							this.nbShip ++;
						}
						else if(this.type == PlanetType.PLAYER || this.type == PlanetType.NEUTRAL){
							this.nbShip--;
							if(this.nbShip < 0){
								this.type = PlanetType.IA;
								this.setColor(new Color(165, 42, 42));
								this.nbShip = this.nbShip *(-1);
							}
						}
					}
					it.remove();
					allItem.remove(ss);
				}
			}
		}
	}

	public void update() {
		if (this.getType() != PlanetType.NEUTRAL)
			nbShip++;
	}

	public void generateShips(Item it, String key) {
		int percentToSend = 0;
		if(key == "UNKNOWN")
			percentToSend = 2;
		else if(key == "CRTL")
			percentToSend = 3;
		else if(key == "SHIFT")
			percentToSend = 1;
		
		int shipsToSend = nbShip/percentToSend;
		for (int i = 0; i < shipsToSend; i++) {
				SpaceShip ss = new SpaceShip(this.center.getX() + 25, this.center.getY() + 25, 10, this);
				ss.setObjective(it);
				shipsAllPlanets.add(ss);
				allItem.add(ss);
		}
		System.out.println("Sent :  " + shipsToSend +" ships");
		nbShip = nbShip - shipsToSend;
	}

	/**
	 * @brief Return the planet at a specific coordinate, null if not.
	 * 
	 */
	public static Planet getPlanetFromCoord(Point2D p) {
		for (Planet plan : allPlanets) {
			if (plan.contains(p))
				return plan;
		}
		return null;
	}

	@Override
	public void draw(Graphics2D arg0) {
		Point2D pos = this.center;
		int x = (int) pos.getX(), y = (int) pos.getY(), w = this.getWidth();
		arg0.setColor(c);
		// arg0.fillRect(x - w / 2, y - w / 2, w, w);
		arg0.fillOval(x - w / 2, y - w / 2, w, w);
		arg0.setColor(Color.black);
		String text = Integer.toString(nbShip);
		arg0.drawString(text, (int) pos.getX() - arg0.getFontMetrics().stringWidth(text) / 2, (int) pos.getY() + 5);
	}

	/**
	 * @brief Found a valid position to place a new planet
	 * @param width
	 *            : width of the window
	 * @param height
	 *            : height of the window
	 * @param planets
	 *            : List of all planet on the map
	 * 
	 */
	public static Point2D findPlanetPosition(int width, int height, ArrayList<Planet> planets, int planetSize) {
		final int nbTry = 1000; // nombre d'essai max pour trouver une pos valide
		final int spacing = planetSize + 30; // on choisi l'espacement entre les plan�tes
		boolean isValidPos = true;
		Random random = new Random();
		Point2D pos = new Point2D.Double(0, 0);

		int nbTryCpt = 0;

		do {
			int x = random.nextInt((width - (planetSize / 2)) + 1 - (planetSize / 2)) + (planetSize / 2);
			int y = random.nextInt((height - (planetSize / 2)) + 1 - (planetSize / 2)) + (planetSize / 2);
			pos.setLocation(x, y);

			// Si c'est la premi�re planete, son emplacement est forcement valide
			if (planets.size() == 0)
				isValidPos = true;

			for (Planet p : planets) {
				if (p.getLocation().distance(pos) < spacing)
					isValidPos = false;
			}

			nbTryCpt++;

		} while (!isValidPos && nbTryCpt < nbTry);

		// Au bout de "nbTry" essais, on arr�te de chercher une position valide et on
		// affiche une erreur
		if (nbTryCpt == nbTry) {
			System.out.println("Impossible de trouver une position valide pour une nouvelle planete");
			pos.setLocation(-1, -1);
		}
		return pos;

	}

	public void updateNeutral() {
		if(this.type == PlanetType.NEUTRAL){
			nbShip++;
		}
	}
	
	
	// IA
	public static void updateIA()
	{
		// 1 : On recup�re la liste des plan�tes poss�der par l'IA
		ArrayList<Planet> listIaPlanets = new ArrayList();
		for (Planet p : allPlanets) {
			if (p.getType() == PlanetType.IA)
				listIaPlanets.add(p);
		}
		
		// 2 : On selectionne une plan�te al�atoire de cette liste
		// Cette plan�te sera la plan�te attaquante
		Random rand = new Random();
		
		// SI l'IA poss�de des plan�tes
		if(listIaPlanets.size() > 0 )
		{
			int planetSelected = rand.nextInt(listIaPlanets.size());
			
			// 3 : On selectionne une plan�te cible
			// Cette plan�te sera la plan�te attaqu�
			// L'IA peut s'envoyer des troupes vers ses propres plan�te
			// dans ce cas la c'est juste un ajout de troupes
			int planetObjectiveSelected = rand.nextInt(allPlanets.size());
			
			// 4 : Si la plan�te cible est la m�me que la plan�te de d�part
			// on selectionne une autre plan�te
			while(listIaPlanets.get(planetSelected) == allPlanets.get(planetObjectiveSelected))
				planetObjectiveSelected = rand.nextInt(allPlanets.size());
			
			// On envoi les vaisseaux
			listIaPlanets.get(planetSelected).setObjective(allPlanets.get(planetObjectiveSelected));
			listIaPlanets.get(planetSelected).generateShips(allPlanets.get(planetObjectiveSelected), "UNKNOWN");
		}
	}

	public String toSave(){
		return this.getLocation().getX() + ";" + this.getLocation().getY() + ";" + this.getWidth() + ";" + this.nbShip + ";" + this.type + ";\n";
	}

	public String toString(){
		return this.getLocation().getX() + ";" + this.getLocation().getY() + ";" + this.getWidth() + ";" + this.nbShip + ";" + this.type;
	}
		
}
